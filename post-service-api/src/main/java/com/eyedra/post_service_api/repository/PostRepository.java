package com.eyedra.post_service_api.repository;

import com.eyedra.post_service_api.entity.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Flux<Post> findByUserId(String userId);
    Flux<Post> findByContentContainingIgnoreCase(String keyword);
}