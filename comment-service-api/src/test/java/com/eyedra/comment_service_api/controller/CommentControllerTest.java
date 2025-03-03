package com.eyedra.comment_service_api.controller;

import com.eyedra.comment_service_api.model.Comment;
import com.eyedra.comment_service_api.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment() {
        Comment comment = new Comment();
        comment.setText("This is a test comment");
        when(commentService.createComment(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentController.createComment(comment);
        verify(commentService, times(1)).createComment(any(Comment.class));
        assert createdComment.getText().equals("This is a test comment");
    }

    @Test
    void testGetComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        when(commentService.getComments()).thenReturn(comments);

        List<Comment> retrievedComments = commentController.getComments();
        verify(commentService, times(1)).getComments();
        assert retrievedComments.size() == 1;
    }
}
