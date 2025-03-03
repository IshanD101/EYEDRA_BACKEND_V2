package com.eyedra.comment_service_api.controller;

import com.eyedra.comment_service_api.model.Comment;
import com.eyedra.comment_service_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        if (comment.getPostId() == null || comment.getPostId().isEmpty()) {
            throw new IllegalArgumentException("Post ID is required");
        }
        comment.setRHeart(false); // Default to false for new comments
        return commentService.createComment(comment);
    }


    @GetMapping
    public List<Comment> getComments() {
        return commentService.getComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable String id, @RequestBody Comment comment) {
        if (comment.getPostId() == null || comment.getPostId().isEmpty()) {
            throw new IllegalArgumentException("Post ID is required");
        }
        return commentService.updateComment(id, comment);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPostId(@PathVariable String postId) {
        return commentService.getCommentsByPostId(postId);
    }

}
