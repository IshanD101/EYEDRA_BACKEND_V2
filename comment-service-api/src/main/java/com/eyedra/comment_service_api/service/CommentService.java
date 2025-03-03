package com.eyedra.comment_service_api.service;

import com.eyedra.comment_service_api.model.Comment;
import com.eyedra.comment_service_api.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    public Comment updateComment(String id, Comment comment) {
        comment.setId(id);
        return commentRepository.save(comment);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
