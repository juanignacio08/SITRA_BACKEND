package com.sitra.sitra.config.websoket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefijo para enviar mensajes desde el cliente al servidor
        registry.setApplicationDestinationPrefixes("/app");

        // Prefijo para suscribirse a canales (topicos) desde el servidor
        registry.enableSimpleBroker("/topic");

        System.out.println("✅ Broker de mensajes configurado");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Este es el endpoint HTTP/WS al que el cliente Angular se conectará para iniciar la comunicación.
        // La URL completa será: ws://localhost:8080/sitra/api/v1/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // **IMPORTANTE:** Ajustar para producción
                .withSockJS();

        System.out.println("✅ WebSocket configurado en /sitra/api/v1/ws");
    }
}
