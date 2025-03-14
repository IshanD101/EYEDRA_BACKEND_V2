package com.eyedra.post_service_api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveWebSocketHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<String> output = sink.asFlux();

        return session.send(output
                .map(session::textMessage))
                .and(session.receive()
                        .map(message -> {
                            String payload = message.getPayloadAsText();
                            logger.info("Received message: {}", payload);
                            sink.tryEmitNext(payload);
                            return payload;
                        }));
    }

    public void broadcastMessage(Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            sink.tryEmitNext(jsonMessage);
        } catch (Exception e) {
            logger.error("Error broadcasting message", e);
        }
    }
}