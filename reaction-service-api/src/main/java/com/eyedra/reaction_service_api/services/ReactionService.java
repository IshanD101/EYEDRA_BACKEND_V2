package com.eyedra.reaction_service_api.services;

import com.eyedra.reaction_service_api.entity.Reaction;
import com.eyedra.reaction_service_api.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eyedra.reaction_service_api.exception.ReactionNotFoundException;


import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {
    public long getTotalReactionsByPostId(String postId) {
        return reactionRepository.countByPostId(postId);
    }


    @Autowired
    private ReactionRepository reactionRepository;

    public Reaction createReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    public List<Reaction> getAllReactions() {
        return reactionRepository.findAll();
    }

    public Optional<Reaction> getReactionById(String id) {
        return reactionRepository.findById(id);
    }

    public Reaction updateReaction(String id, Reaction reaction) {
        if (!reactionRepository.existsById(id)) {
            throw new ReactionNotFoundException("Reaction not found with id: " + id);
        }

        reaction.setId(id);
        return reactionRepository.save(reaction);
    }

    public void deleteReaction(String id) {
        if (!reactionRepository.existsById(id)) {
            throw new ReactionNotFoundException("Reaction not found with id: " + id);
        }
        reactionRepository.deleteById(id);

    }
}
