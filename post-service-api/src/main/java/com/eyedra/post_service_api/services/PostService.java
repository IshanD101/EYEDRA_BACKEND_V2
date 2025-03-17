package com.eyedra.post_service_api.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.eyedra.post_service_api.dto.PostRequestDTO;
import com.eyedra.post_service_api.dto.PostResponseDTO;
import com.eyedra.post_service_api.dto.PostSearchDTO;
import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final Cloudinary cloudinary;

    public Mono<PostResponseDTO> createPost(Mono<PostRequestDTO> postRequestDTO, Mono<FilePart> imageFile) {
        return postRequestDTO.flatMap(dto ->
            uploadImage(imageFile)
                .flatMap(imageUrl -> {
                    Post post = new Post(null, dto.getUserId(),
                            dto.getContent(), imageUrl, new Date());
                    return postRepository.save(post);
                })
                .map(this::convertToDto)
        );
    }

    public Flux<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Flux<Post> getPostsByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    public Mono<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public Mono<PostResponseDTO> updatePost(String id, PostRequestDTO postRequestDTO, Mono<FilePart> imageFile) {
        return postRepository.findById(id)
                .flatMap(existingPost ->
                    uploadImage(imageFile)
                        .flatMap(imageUrl -> {
                            existingPost.setContent(postRequestDTO.getContent());
                            if (!imageUrl.isEmpty()) {
                                existingPost.setImageUrl(imageUrl);
                            }
                            return postRepository.save(existingPost);
                        })
                ).map(this::convertToDto);
    }

    public Mono<Void> deletePost(String id) {
        return postRepository.deleteById(id);
    }

    public Flux<Post> searchPosts(PostSearchDTO searchDTO) {
        Flux<Post> result = postRepository.findAll(); // Default query
        
        if (searchDTO.getTitle() != null && !searchDTO.getTitle().isEmpty()) {
            result = result.filter(post -> post.getContent().toLowerCase().contains(searchDTO.getTitle().toLowerCase()));
        }
    
        if (searchDTO.getAuthor() != null && !searchDTO.getAuthor().isEmpty()) {
            result = result.filter(post -> post.getUserId().equalsIgnoreCase(searchDTO.getAuthor()));
        }
    
        if (searchDTO.getStartDate() != null && searchDTO.getEndDate() != null) {
            Date startDate = parseDate(searchDTO.getStartDate());
            Date endDate = parseDate(searchDTO.getEndDate());
            
            if (startDate != null && endDate != null) {
                result = result.filter(post -> 
                    !post.getCreatedAt().before(startDate) && !post.getCreatedAt().after(endDate));
            }
        }
    
        return result;
    }
    
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(dateStr);
        } catch (ParseException e) {
            log.error("Error parsing date: {}", e.getMessage());
            return null;
        }
    }

    private Mono<String> uploadImage(Mono<FilePart> filePart) {
        return filePart
            .flatMap(file -> file.content()
                .reduce(new byte[0], (acc, dataBuffer) -> {
                    byte[] bytes = new byte[acc.length + dataBuffer.readableByteCount()];
                    System.arraycopy(acc, 0, bytes, 0, acc.length);
                    dataBuffer.read(bytes, acc.length, dataBuffer.readableByteCount());
                    return bytes;
                }))
            .flatMap(bytes -> Mono.fromCallable(() -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
                return (String) uploadResult.get("secure_url");
            }))
            .onErrorResume(e -> {
                log.error("Image upload failed: {}", e.getMessage());
                return Mono.just("");
            })
            .switchIfEmpty(Mono.just("")); // returns empty string if no image is provided
    }

    private PostResponseDTO convertToDto(Post post) {
        // Create a new PostResponseDTO with the available fields
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}