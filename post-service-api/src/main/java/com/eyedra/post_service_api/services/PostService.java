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
        if (searchDTO.getTitle() != null && !searchDTO.getTitle().isEmpty()) {
            return postRepository.findByTitle(searchDTO.getTitle());
        } else if (searchDTO.getTags() != null && !searchDTO.getTags().isEmpty()) {
            return postRepository.findByTags(searchDTO.getTags());
        } else if (searchDTO.getAuthor() != null && !searchDTO.getAuthor().isEmpty()) {
            return postRepository.findByAuthor(searchDTO.getAuthor());
        } else if (searchDTO.getStartDate() != null && searchDTO.getEndDate() != null) {
            return postRepository.findByDateRange(searchDTO.getStartDate(), searchDTO.getEndDate());
        }
        return postRepository.findAll();
    }

    private Mono<String> uploadImage(Mono<FilePart> filePart) {
        if (filePart == null) {
            return Mono.just("");
        }
        return filePart
            .flatMap(file -> file.content()
                .reduce(new byte[0], (acc, dataBuffer) -> {
                    byte[] bytes = new byte[acc.length + dataBuffer.readableByteCount()];
                    System.arraycopy(acc, 0, bytes, 0, acc.length);
                    dataBuffer.read(bytes, acc.length, dataBuffer.readableByteCount());
                    return bytes;
                }))
            .flatMap(bytes -> Mono.fromCallable(() -> {
                Map uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
                return (String) uploadResult.get("secure_url");
            }))
            .onErrorResume(e -> {
                log.error("Image upload failed: {}", e.getMessage());
                return Mono.just("");
            });
    }

    private PostResponseDTO convertToDto(Post post) {
        return new PostResponseDTO(post.getId(), post.getUserId(),
                post.getContent(), post.getImageUrl(), post.getCreatedAt());
    }
}
