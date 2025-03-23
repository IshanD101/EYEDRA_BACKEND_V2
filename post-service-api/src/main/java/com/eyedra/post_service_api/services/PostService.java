package com.eyedra.post_service_api.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Cloudinary cloudinary;

    public Mono<Post> createPost(Post post, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                post.setImageUrl(uploadResult.get("url").toString());
            } catch (IOException e) {
                return Mono.error(new RuntimeException("Image upload failed", e));
            }
        }
        post.setCreatedAt(new Date());
        return postRepository.save(post);
    }

    public Mono<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public Flux<Post> getPostsByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    public Flux<Post> searchPosts(String keyword) {
        return postRepository.findByContentContainingIgnoreCase(keyword);
    }

    public Mono<Void> deletePost(String id) {
        return postRepository.deleteById(id);
    }

    public Mono<Post> updatePost(String id, Post updatedPost) {
        return postRepository.findById(id)
                .flatMap(existingPost -> {
                    existingPost.setContent(updatedPost.getContent());
                    existingPost.setTitle(updatedPost.getTitle());
                    existingPost.setImageUrl(updatedPost.getImageUrl());
                    return postRepository.save(existingPost);
                });
    }
    public Flux<Post> getAllPosts() {
        return postRepository.findAll();
    }
}