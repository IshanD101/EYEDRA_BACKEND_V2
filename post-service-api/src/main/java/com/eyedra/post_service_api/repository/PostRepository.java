package com.eyedra.post_service_api.repository;

import com.eyedra.post_service_api.entity.Post;

import reactor.core.publisher.Flux;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Flux<Post> findByUserId(String userId);

    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    Flux<Post> findByTitle(String title);

    @Query("{'tags': {$in: ?0}}")
    Flux<Post> findByTags(List<String> tags);

    @Query("{'author': ?0}")
    Flux<Post> findByAuthor(String author);

    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    Flux<Post> findByDateRange(String startDate, String endDate);
}
