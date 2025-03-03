package com.eyedra.post_service_api.services;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostDTO;
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


    public Post createPost(PostDTO postDTO, MultipartFile imageFile) {
        String imageUrl = uploadImage(imageFile);

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthor(postDTO.getAuthor());
        post.setUserId(postDTO.getUserId());
        post.setImageUrl(imageUrl);

        post.setCreatedAt(LocalDateTime.now());

        logger.info("Creating post: {}", post);
        return postRepository.save(post);
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

    public Post updatePost(String id, PostDTO postDTO, MultipartFile imageFile) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (postDTO.getTitle() != null) post.setTitle(postDTO.getTitle());
        if (postDTO.getContent() != null) post.setContent(postDTO.getContent());
        if (postDTO.getAuthor() != null) post.setAuthor(postDTO.getAuthor());
        if (postDTO.getUserId() != null) post.setUserId(postDTO.getUserId());

        String imageUrl = uploadImage(imageFile);
        if (imageUrl != null) post.setImageUrl(imageUrl);


        logger.info("Updating post: {}", post);
        return postRepository.save(post);
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
            logger.debug("Upload result: {}", uploadResult); // Added logging for upload result
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
