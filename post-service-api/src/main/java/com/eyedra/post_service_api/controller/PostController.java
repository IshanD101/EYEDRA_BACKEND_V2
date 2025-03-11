package com.eyedra.post_service_api.controller;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostRequestDTO;
import com.eyedra.post_service_api.dto.PostResponseDTO;
import com.eyedra.post_service_api.services.PostService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Mono<ResponseEntity<PostResponseDTO>> createPost(@Valid @ModelAttribute PostRequestDTO postRequestDTO,
                                                            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        return postService.createPost(postRequestDTO, imageFile)
                .map(post -> ResponseEntity.status(HttpStatus.CREATED).body(post));
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
                                                            @Valid @ModelAttribute PostRequestDTO postRequestDTO,
                                                            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        return postService.updatePost(id, postRequestDTO, imageFile)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable String id) {
        return postService.deletePost(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
