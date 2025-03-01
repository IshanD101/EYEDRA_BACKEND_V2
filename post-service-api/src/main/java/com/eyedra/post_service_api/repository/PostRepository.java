package com.eyedra.post_service_api.repository;

import com.eyedra.post_service_api.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(String userId);

}
