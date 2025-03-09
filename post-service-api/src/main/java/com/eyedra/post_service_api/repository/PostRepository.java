package com.eyedra.post_service_api.repository;

import com.eyedra.post_service_api.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(String userId);


    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    List<Post> findByTitle(String title);
    @Query("{'tags': {$in: ?0}}")
    List<Post> findByTags(List<String> tags);
    @Query("{'author': ?0}")
    List<Post> findByAuthor(String author);
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Post> findByDateRange(String startDate, String endDate);
}
