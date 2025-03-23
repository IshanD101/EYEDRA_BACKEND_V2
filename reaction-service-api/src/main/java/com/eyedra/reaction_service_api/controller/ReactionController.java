package com.eyedra.reaction_service_api.controller;

import com.eyedra.reaction_service_api.entity.Reaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.eyedra.reaction_service_api.services.ReactionService;
import com.eyedra.reaction_service_api.services.SocketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eyedra.reaction_service_api.exception.ReactionNotFoundException;
import com.eyedra.reaction_service_api.dto.RequestDTO;
import com.eyedra.reaction_service_api.dto.ResponseDTO;
import com.eyedra.reaction_service_api.dto.CreateReactionDTO;

@RestController
@RequestMapping("/api/v1/reactions")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @Autowired
    private SocketService socketService;

    @PostMapping
    public Mono<ResponseEntity<Reaction>> createReaction(@RequestBody CreateReactionDTO createReactionDTO) {
        Reaction reaction = new Reaction();
        reaction.setUserId(createReactionDTO.getUserId());
        reaction.setPostId(createReactionDTO.getPostId());
        reaction.setHrt(createReactionDTO.isHrt());
        return reactionService.createReaction(reaction)
            .doOnNext(r -> socketService.sendMessage("New reaction created: " + r.toString()))
            .map(ResponseEntity::ok);
    }

    @GetMapping("/count/{postId}")
    public Mono<ResponseEntity<ResponseDTO>> getTotalReactionsByPostId(@PathVariable String postId) {
        return reactionService.getTotalReactionsByPostId(postId)
            .map(count -> {
                ResponseDTO responseDTO = new ResponseDTO();
                responseDTO.setTotalReactions(count);
                return ResponseEntity.ok(responseDTO);
            });
    }

    @GetMapping
    public Flux<Reaction> getAllReactions() {
        return reactionService.getAllReactions();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Reaction>> getReactionById(@PathVariable String id) {
        return reactionService.getReactionById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Reaction>> updateReaction(@PathVariable String id, @RequestBody Reaction reaction) {
        socketService.sendMessage("Reaction updated: " + reaction.toString());
        return reactionService.updateReaction(id, reaction)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteReaction(@PathVariable String id) {
        return reactionService.deleteReaction(id)
        .then(Mono.just(ResponseEntity.noContent().build()))
        .onErrorResume(ReactionNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().build())); // Handle exception properly
    }
}
