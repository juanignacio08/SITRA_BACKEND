package com.sitra.sitra.service.websocket;

import com.sitra.sitra.expose.response.reportes.NoticiasResponse;
import com.sitra.sitra.expose.response.turnos.OrdenAtencionResponse;
import com.sitra.sitra.expose.response.turnos.PantallaResponse;
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
            log.error("Error WebSocket saveOrder: {}", e.getMessage());
        }
    }

    public void notificarNuevaLlamada(PantallaResponse pantallaResponse) {
        try {
            messagingTemplate.convertAndSend("/topic/nueva-llamada", pantallaResponse);
            log.info("📢 WebSocket: Nueva llamada ID: {}", pantallaResponse.getLlamadaId());
        } catch (Exception e) {
            log.error("Error WebSocket callNext: {}", e.getMessage());
        }
    }

    public void notificarNuevaAtencion(PantallaResponse pantallaResponse) {
        try {
            messagingTemplate.convertAndSend("/topic/nueva-atencion", pantallaResponse);
            log.info("📢 WebSocket: Nueva Atencion ID: {}", pantallaResponse.getAtencionId());
        } catch (Exception e) {
            log.error("Error WebSocket saveAttention: {}", e.getMessage());
        }
    }

    public void notificarFinalizarAtencion(PantallaResponse pantallaResponse) {
        try {
            messagingTemplate.convertAndSend("/topic/finalizar-atencion", pantallaResponse);
            log.info("📢 WebSocket: Finalizar Atencion ID: {}", pantallaResponse.getAtencionId());
        } catch (Exception e) {
            log.error("Error WebSocket finishAttention: {}", e.getMessage());
        }
    }

    public void notificarNuevaAusencia(PantallaResponse pantallaResponse) {
        try {
            messagingTemplate.convertAndSend("/topic/nueva-ausencia", pantallaResponse);
            log.info("📢 WebSocket: Nueva Ausencia ID: {}", pantallaResponse.getAtencionId());
        } catch (Exception e) {
            log.error("Error WebSocket markAsAbsent: {}", e.getMessage());
        }
    }

    public void notificarNuevaNoticia(NoticiasResponse noticiasResponse) {
        try {
            messagingTemplate.convertAndSend("/topic/nueva-noticia", noticiasResponse);
            log.info("📢 WebSocket: Nueva Noticia ID: {}", noticiasResponse.getNoticiasId());
        } catch (Exception e) {
            log.error("Error WebSocket saveNotice: {}", e.getMessage());
        }
    }

    public void notificarNuevaEdicionNoticia(NoticiasResponse noticiasResponse) {
        try {
            messagingTemplate.convertAndSend("/topic/noticia-edicion", noticiasResponse);
            log.info("📢 WebSocket: Noticia Editada ID: {}", noticiasResponse.getNoticiasId());
        } catch (Exception e) {
            log.error("Error WebSocket updateNotice: {}", e.getMessage());
        }
    }

    public void notificarEliminacionNoticia(NoticiasResponse noticiasResponse) {
        try {
            messagingTemplate.convertAndSend("/topic/noticia-eliminada", noticiasResponse);
            log.info("📢 WebSocket: Noticia Eliminada ID: {}", noticiasResponse.getNoticiasId());
        } catch (Exception e) {
            log.error("Error WebSocket deleteNotice: {}", e.getMessage());
        }
    }
}
