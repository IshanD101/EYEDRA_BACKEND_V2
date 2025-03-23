package com.eyedra.reaction_service_api.repository;

import com.eyedra.reaction_service_api.entity.Reaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends ReactiveMongoRepository<Reaction, String> {
    Mono<Long> countByPostId(String postId);
}
