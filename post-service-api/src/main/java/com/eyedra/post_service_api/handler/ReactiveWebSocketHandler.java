package com.eyedra.post_service_api.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveWebSocketHandler.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull WebSocketSession session) {
        sessions.put(session.getId(), session);
        logger.info("Client connected: {}", session.getId());

        Flux<String> output = sink.asFlux();

        return session.send(output.map(session::textMessage))
                .and(session.receive()
                        .map(message -> {
                            String payload = message.getPayloadAsText();
                            logger.info("Received: {}", payload);
                            broadcastToOthers(payload, session.getId());
                            return payload;
                        })
                        .then())
                .doFinally(signalType -> {
                    sessions.remove(session.getId());
                    logger.info("Client disconnected: {}", session.getId());
                });
    }

    private void broadcastToOthers(String message, String senderId) {
        Flux.fromIterable(sessions.entrySet())
                .filter(entry -> !entry.getKey().equals(senderId))
                .flatMap(entry -> entry.getValue().send(Mono.just(entry.getValue().textMessage(message))))
                .subscribe(null, error -> logger.error("Broadcast error", error));
    }

    public void broadcastMessage(Object message) {
        try {
            sink.tryEmitNext(objectMapper.writeValueAsString(message));
            logger.info("Broadcasting: {}", message);
        } catch (JsonProcessingException e) {
            logger.error("Broadcast error", e);
        }
    }
}
