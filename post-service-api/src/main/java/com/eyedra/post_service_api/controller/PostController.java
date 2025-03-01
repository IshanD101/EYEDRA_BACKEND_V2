package com.eyedra.post_service_api.controller;

import com.eyedra.post_service_api.entity.Post;
import com.eyedra.post_service_api.dto.PostDTO;
import com.eyedra.post_service_api.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           @RequestParam("author") String author,
                                           @RequestParam("userId") String userId,
                                           @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        if (title == null || title.isEmpty() || content == null || content.isEmpty() || author == null || author.isEmpty() || userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(title);
        postDTO.setContent(content);
        postDTO.setAuthor(author);
        postDTO.setUserId(userId);

        Post createdPost = postService.createPost(postDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }


    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable String userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id,
                                           @RequestParam(value = "title", required = false) String title,
                                           @RequestParam(value = "content", required = false) String content,
                                           @RequestParam("author") String author,
                                           @RequestParam("userId") String userId,
                                           @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        if (author == null || author.isEmpty() || userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(title);
        postDTO.setContent(content);
        postDTO.setAuthor(author);
        postDTO.setUserId(userId);

        Post updatedPost = postService.updatePost(id, postDTO, imageFile);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
