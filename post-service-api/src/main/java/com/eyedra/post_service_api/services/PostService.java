package com.eyedra.post_service_api.services;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostRequestDTO;
import com.eyedra.post_service_api.dto.PostResponseDTO;

import com.eyedra.post_service_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Cloudinary cloudinary; 

public PostResponseDTO createPost(PostRequestDTO postRequestDTO, MultipartFile imageFile) {
    String userId = postRequestDTO.getUserId(); // Extract userId from request

        String imageUrl = uploadImage(imageFile);

        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        post.setUserId(userId); // Set userId in the post entity
        post.setImageUrl(imageUrl); 

        post.setCreatedAt(LocalDateTime.now());

        logger.info("Creating post: {}", post);
        postRepository.save(post);

        return new PostResponseDTO(post.getTitle(), post.getContent(), post.getUserId());
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public PostResponseDTO updatePost(String id, PostRequestDTO postRequestDTO, MultipartFile imageFile) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (postRequestDTO.getTitle() != null) post.setTitle(postRequestDTO.getTitle());
        if (postRequestDTO.getContent() != null) post.setContent(postRequestDTO.getContent());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadImage(imageFile);
            post.setImageUrl(imageUrl); // Update imageUrl only if a new image is provided
        }


        logger.info("Updating post: {}", post);
        postRepository.save(post);
        return new PostResponseDTO(post.getTitle(), post.getContent(), post.getUserId());
    }

    public void deletePost(String id) {
        postRepository.deleteById(id);
        logger.info("Deleted post with id: {}", id);
    }

    private String uploadImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            logger.error("No image file provided");
            return null;
        }

        if (!isValidImageFile(imageFile)) {
            logger.error("Invalid image file: {}", imageFile.getOriginalFilename());
            logger.error("File type: {}, File size: {}", imageFile.getContentType(), imageFile.getSize());
            throw new RuntimeException("Invalid image file");
        }

        try {
            String uniqueID = UUID.randomUUID().toString();
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "image", "public_id", uniqueID));

            String imageUrl = (String) uploadResult.get("url");
            logger.info("Image uploaded to: {}", imageUrl);
            logger.debug("Upload result: {}", uploadResult);
            return imageUrl;

        } catch (IOException e) {
            logger.error("Error uploading image: {}", e.getMessage());
            throw new RuntimeException("Image upload failed", e);
        }
    }

    private boolean isValidImageFile(MultipartFile imageFile) {
        String contentType = imageFile.getContentType();
        long size = imageFile.getSize();
        return (contentType != null && (contentType.startsWith("image/"))) && size <= 5 * 1024 * 1024; // 5 MB limit
    }
}
