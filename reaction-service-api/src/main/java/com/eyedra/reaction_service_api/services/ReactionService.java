package com.eyedra.reaction_service_api.services;

import com.eyedra.reaction_service_api.entity.Reaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.eyedra.reaction_service_api.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eyedra.reaction_service_api.exception.ReactionNotFoundException;


@Service
public class ReactionService {
    public Mono<Long> getTotalReactionsByPostId(String postId) {
        return reactionRepository.countByPostId(postId).defaultIfEmpty(0L);


    }


    @Autowired
    private ReactionRepository reactionRepository;

    public Mono<Reaction> createReaction(Reaction reaction) {
        return reactionRepository.save(reaction);

    }

    public Flux<Reaction> getAllReactions() {
        return reactionRepository.findAll();

    }

    public Mono<Reaction> getReactionById(String id) {
        return reactionRepository.findById(id);

    }

    public Mono<Reaction> updateReaction(String id, Reaction reaction) {
        return reactionRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new ReactionNotFoundException("Reaction not found with id: " + id));
            }

            reaction.setId(id);
            return reactionRepository.save(reaction);
        });

    }

    public Mono<Void> deleteReaction(String id) {
        return reactionRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new ReactionNotFoundException("Reaction not found with id: " + id));
            }
            return reactionRepository.deleteById(id);
        });

    }
}
