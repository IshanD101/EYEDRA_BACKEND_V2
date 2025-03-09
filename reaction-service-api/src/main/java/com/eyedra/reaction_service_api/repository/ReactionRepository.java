package com.eyedra.reaction_service_api.repository;

import com.eyedra.reaction_service_api.entity.Reaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends MongoRepository<Reaction, String> {
    long countByPostId(String postId);

}
