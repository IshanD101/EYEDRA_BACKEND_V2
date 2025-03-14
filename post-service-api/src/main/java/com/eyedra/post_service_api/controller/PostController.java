package com.eyedra.post_service_api.controller;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostRequestDTO;
import com.eyedra.post_service_api.dto.PostResponseDTO;
import com.eyedra.post_service_api.dto.PostSearchDTO;
import com.eyedra.post_service_api.services.PostService;
import com.eyedra.post_service_api.handler.ReactiveWebSocketHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final ReactiveWebSocketHandler webSocketHandler;

    public PostController(PostService postService, ReactiveWebSocketHandler webSocketHandler) {
        this.postService = postService;
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Object> createPost(
            @RequestPart("post") Mono<PostRequestDTO> postRequestDTO,
            @RequestPart(value = "image", required = false) Mono<FilePart> imageFile) {

        return postService.createPost(postRequestDTO, imageFile)
                .doOnSuccess(post -> webSocketHandler.broadcastMessage(post))
                .map(post -> ResponseEntity.ok(post));
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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<PostResponseDTO>> updatePost(
            @PathVariable String id,
            @RequestPart("post") PostRequestDTO postRequestDTO,
            @RequestPart(value = "image", required = false) Mono<FilePart> imageFile) {
        return postService.updatePost(id, postRequestDTO, imageFile)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable String id) {
        return postService.deletePost(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PostMapping("/search")
    public Flux<Post> searchPosts(@RequestBody PostSearchDTO searchDTO) {
        return postService.searchPosts(searchDTO);
    }

    @GetMapping("/search")
    public Flux<Post> searchPostsWithParams(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        PostSearchDTO searchDTO = new PostSearchDTO();
        searchDTO.setTitle(title);
        searchDTO.setTags(tags);
        searchDTO.setAuthor(author);
        searchDTO.setStartDate(startDate);
        searchDTO.setEndDate(endDate);

        return postService.searchPosts(searchDTO);
    }
}
