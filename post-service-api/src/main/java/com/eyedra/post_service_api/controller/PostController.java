package com.eyedra.post_service_api.controller;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostRequestDTO;
import com.eyedra.post_service_api.dto.PostResponseDTO;
import com.eyedra.post_service_api.services.PostService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Mono<Object>> createPost(@RequestParam("title") String title,
                                                     @RequestParam("content") String content,
                                                     @RequestParam("userId") String userId, // Added userId parameter
                                                     @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        if (title == null || title.isEmpty() || content == null || content.isEmpty() || userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (imageFile != null && !isValidImageFile(imageFile)) {
            return ResponseEntity.badRequest().body(null); // Invalid image file
        }

        PostRequestDTO postRequestDTO = new PostRequestDTO();
        postRequestDTO.setTitle(title);
        postRequestDTO.setContent(content);
        postRequestDTO.setUserId(userId); // Set userId in the DTO

        Mono<Object> createdPost = postService.createPost(postRequestDTO, imageFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping
    public Flux<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/user/{userId}")
    public Flux<Post> getPostsByUserId(@PathVariable String userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Post>> getPostById(@PathVariable String id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PostResponseDTO>> updatePost(@PathVariable String id,
                                                     @RequestParam(value = "title", required = false) String title,
                                                     @RequestParam(value = "content", required = false) String content,
                                                     @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        if (id == null || id.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        if (imageFile != null && !isValidImageFile(imageFile)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        PostRequestDTO postRequestDTO = new PostRequestDTO();
        postRequestDTO.setTitle(title);
        postRequestDTO.setContent(content);

        return postService.updatePost(id, postRequestDTO, imageFile)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable String id) {
        return postService.deletePost(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    private boolean isValidImageFile(MultipartFile imageFile) {
        String contentType = imageFile.getContentType();
        long size = imageFile.getSize();

        return (contentType != null && (contentType.startsWith("image/"))) && size <= 5 * 1024 * 1024; // 5 MB limit
    }
}
