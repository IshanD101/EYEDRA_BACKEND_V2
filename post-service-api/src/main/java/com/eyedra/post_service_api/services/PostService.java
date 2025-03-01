package com.eyedra.post_service_api.services;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostDTO;
import com.eyedra.post_service_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    public Post createPost(PostDTO postDTO, MultipartFile imageFile) {
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            String uniqueID = UUID.randomUUID().toString();
            String fileName = uniqueID + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get("src/main/resources/images/" + fileName);
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());
                imagePath = path.toString();
                logger.info("Image saved at: {}", imagePath);
            } catch (IOException e) {
                logger.error("Error saving image: {}", e.getMessage());
            }
        }

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthor(postDTO.getAuthor());
        post.setUserId(postDTO.getUserId());
        post.setImagePath(imagePath);
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
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (postDTO.getTitle() != null) {
            post.setTitle(postDTO.getTitle());
        }
        if (postDTO.getContent() != null) {
            post.setContent(postDTO.getContent());
        }
        if (postDTO.getAuthor() != null) {
            post.setAuthor(postDTO.getAuthor());
        }
        if (postDTO.getUserId() != null) {
            post.setUserId(postDTO.getUserId());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String uniqueID = UUID.randomUUID().toString();
            String fileName = uniqueID + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get("src/main/resources/images/" + fileName);
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());
                post.setImagePath(path.toString());
                logger.info("Image updated at: {}", post.getImagePath());
            } catch (IOException e) {
                logger.error("Error saving image: {}", e.getMessage());
            }
        }

        logger.info("Updating post: {}", post);
        return postRepository.save(post);
    }

    public void deletePost(String id) {
        postRepository.deleteById(id);
        
        logger.info("Deleted post with id: {}", id);
    }
}
