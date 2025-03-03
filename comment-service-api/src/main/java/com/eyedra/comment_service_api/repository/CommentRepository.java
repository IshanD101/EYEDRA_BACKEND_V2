package com.eyedra.comment_service_api.repository;

import com.eyedra.comment_service_api.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
    // Additional query methods can be defined here if needed
}
