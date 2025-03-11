package com.eyedra.post_service_api.services;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostRequestDTO;
import com.eyedra.post_service_api.dto.PostResponseDTO;
import com.eyedra.post_service_api.dto.PostSearchDTO;
import com.eyedra.post_service_api.repository.PostRepository;


import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.function.client.WebClient;




@Service
@RequiredArgsConstructor
@EnableReactiveMongoRepositories(basePackages = "com.eyedra.post_service_api.repository")
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;
    private final MongoTemplate mongoTemplate;
    private WebClient webClient;

    private Cloudinary cloudinary;
    private String userServiceUrl = "http://localhost:8761/"; // UserService URL

    public Mono<Object> createPost(PostRequestDTO postRequestDTO, MultipartFile imageFile) {
        return getUsername(postRequestDTO.getUserId())
            .flatMap(author -> {
                return uploadImage(imageFile).switchIfEmpty(Mono.just(""))
                .flatMap(imageUrl -> {
                    Post post = new Post();
                    post.setTitle(postRequestDTO.getTitle());
                    post.setContent(postRequestDTO.getContent());
                    post.setAuthor(author);
                    post.setUserId(postRequestDTO.getUserId());
                    post.setImageUrl(imageUrl);
                    post.setCreatedAt(LocalDateTime.now());

                    logger.info("Creating post: {}", post);
                    return postRepository.save(post)
                        .map(savedPost -> new PostResponseDTO(
                            savedPost.getTitle(),
                            savedPost.getContent(),
                            savedPost.getAuthor(),
                            savedPost.getUserId()
                        ));
                });
            });
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

    public Mono<PostResponseDTO> updatePost(String id, PostRequestDTO postRequestDTO, MultipartFile imageFile) {
        return postRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Post not found")))
            .flatMap(post -> {
                if (postRequestDTO.getTitle() != null) post.setTitle(postRequestDTO.getTitle());
                if (postRequestDTO.getContent() != null) post.setContent(postRequestDTO.getContent());

                if (imageFile != null && !imageFile.isEmpty()) {
                    return uploadImage(imageFile)
                        .map(imageUrl -> {
                            post.setImageUrl(imageUrl);
                            return post;
                        });
                }
                return Mono.just(post);
            })
            .flatMap(postRepository::save)
            .map(savedPost -> new PostResponseDTO(
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getUserId(),
                savedPost.getAuthor()
            ));
    }

    public Mono<Void> deletePost(String id) {
        return postRepository.deleteById(id);
    }

    private Mono<String> uploadImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            logger.warn("No image file provided");
            return Mono.empty();
        }

        if (!isValidImageFile(imageFile)) {
            logger.error("Invalid image file: {}", imageFile.getOriginalFilename());
            return Mono.error(new RuntimeException("Invalid image file"));
        }

        try {
            String uniqueID = UUID.randomUUID().toString();
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "image", "public_id", uniqueID));
            String imageUrl = (String) uploadResult.get("url");
            logger.info("Image uploaded to: {}", imageUrl);
            return Mono.justOrEmpty(imageUrl);

        } catch (IOException e) {
            logger.error("Error uploading image: {}", e.getMessage());
            return Mono.error(new RuntimeException("Image upload failed", e));
        }
    }


    private boolean isValidImageFile(MultipartFile imageFile) {
        String contentType = imageFile.getContentType();
        long size = imageFile.getSize();
        return (contentType != null && (contentType.startsWith("image/"))) && size <= 5 * 1024 * 1024; // 5 MB limit
    }

    // Update searchPosts to be reactive
    public Flux<Post> searchPosts(PostSearchDTO searchDTO) {
        Query query = new Query();

        if (searchDTO.getTitle() != null && !searchDTO.getTitle().isEmpty()) {
            query.addCriteria(Criteria.where("title").regex(searchDTO.getTitle(), "i"));
        }
        if (searchDTO.getAuthor() != null && !searchDTO.getAuthor().isEmpty()) {
            query.addCriteria(Criteria.where("author").is(searchDTO.getAuthor()));
        }
        if (searchDTO.getTags() != null && !searchDTO.getTags().isEmpty()) {
            query.addCriteria(Criteria.where("tags").in(searchDTO.getTags()));
        }
        if (searchDTO.getStartDate() != null && searchDTO.getEndDate() != null) {
            LocalDate start = LocalDate.parse(searchDTO.getStartDate(), DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(searchDTO.getEndDate(), DateTimeFormatter.ISO_DATE);
            query.addCriteria(Criteria.where("createdAt").gte(start).lte(end));
        }

        return Flux.fromIterable(mongoTemplate.find(query, Post.class));
    }
    private Mono<String> getUsername(String userId) {
        try {
            return webClient.get()
                    .uri(userServiceUrl + "/api/v1/users/" + userId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(e -> logger.error("Failed to fetch username for userId: {}", userId, e));
        } catch (Exception e) {
            logger.error("Failed to fetch username for userId: {}", userId, e);
            throw new RuntimeException("Failed to fetch username from UserService");
        }
    }
}
