package com.eyedra.reaction_service_api.controller;

import com.eyedra.reaction_service_api.entity.Reaction;
import com.eyedra.reaction_service_api.services.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eyedra.reaction_service_api.exception.ReactionNotFoundException;
import com.eyedra.reaction_service_api.dto.RequestDTO;
import com.eyedra.reaction_service_api.dto.ResponseDTO;
import com.eyedra.reaction_service_api.dto.CreateReactionDTO;
import com.eyedra.reaction_service_api.dto.DeleteReactionDTO;




import java.util.List;

@RestController
@RequestMapping("/api/v1/reactions")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public Reaction createReaction(@RequestBody CreateReactionDTO createReactionDTO) {
        Reaction reaction = new Reaction();
        reaction.setUserId(createReactionDTO.getUserId());
        reaction.setPostId(createReactionDTO.getPostId());
        reaction.setHrt(createReactionDTO.isHrt());
        return reactionService.createReaction(reaction);
    }

    @PostMapping("/count")
    public ResponseEntity<ResponseDTO> getTotalReactionsByPostId(@RequestBody RequestDTO requestDTO) {
        long count = reactionService.getTotalReactionsByPostId(requestDTO.getPostId());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTotalReactions(count);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public List<Reaction> getAllReactions() {
        return reactionService.getAllReactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reaction> getReactionById(@PathVariable String id) {
        return reactionService.getReactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reaction> updateReaction(@PathVariable String id, @RequestBody Reaction reaction) {
        try {
            Reaction updatedReaction = reactionService.updateReaction(id, reaction);
            return ResponseEntity.ok(updatedReaction);
        } catch (ReactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteReaction(@RequestBody DeleteReactionDTO deleteReactionDTO) {
        try {
            reactionService.deleteReaction(deleteReactionDTO.getReactionId());
            return ResponseEntity.noContent().build();
        } catch (ReactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable String id) {
        try {
            reactionService.deleteReaction(id);
            return ResponseEntity.noContent().build();
        } catch (ReactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
