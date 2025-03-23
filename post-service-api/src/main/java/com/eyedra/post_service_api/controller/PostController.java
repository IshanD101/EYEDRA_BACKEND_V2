package com.eyedra.post_service_api.controller;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// import java.io.IOException;
// import java.util.Date;
// import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Post> createPostFromJson(@RequestBody Post post) {
        return postService.createPost(post, null); // Pass null for the image
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Post> createPostFromMultipart(
            @RequestPart("post") Post post,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return postService.createPost(post, image);
    }

    @GetMapping("/{id}")
    public Mono<Post> getPostById(@PathVariable String id) {
        return postService.getPostById(id);
    }

    @GetMapping("/user/{userId}")
    public Flux<Post> getPostsByUserId(@PathVariable String userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/search")
    public Flux<Post> searchPosts(@RequestParam String keyword) {
        return postService.searchPosts(keyword);
    }

    @GetMapping
    public Flux<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PutMapping("/{id}")
    public Mono<Post> updatePost(@PathVariable String id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePost(@PathVariable String id) {
        return postService.deletePost(id);
    }
}