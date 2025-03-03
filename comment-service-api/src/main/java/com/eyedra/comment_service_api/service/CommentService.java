package com.eyedra.comment_service_api.service;

import com.eyedra.comment_service_api.model.Comment;
import com.eyedra.comment_service_api.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime; // Import for date and time
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now()); // Set creation timestamp
        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    @Transactional
    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    public Comment updateComment(String id, Comment comment) {
        comment.setUpdatedAt(LocalDateTime.now()); // Set update timestamp
        comment.setId(id);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }
}
