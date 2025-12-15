package com.sitra.sitra.service.websocket;

import com.sitra.sitra.expose.response.turnos.OrdenAtencionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notificarNuevaOrden(OrdenAtencionResponse ordenResponse) {
        try {
            // Envía DIRECTAMENTE tu OrdenAtencionResponse
            messagingTemplate.convertAndSend("/topic/nuevas-ordenes", ordenResponse);
            log.info("📢 WebSocket: Nueva orden ID: {}", ordenResponse.getOrdenAtencionId());
        } catch (Exception e) {
            log.error("Error WebSocket: {}", e.getMessage());
        }
    }
}
