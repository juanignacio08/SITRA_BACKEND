package com.sitra.sitra.service.reportes.Impl;

import com.sitra.sitra.entity.reportes.NoticiasEntity;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.reportes.NoticiasRequest;
import com.sitra.sitra.expose.response.reportes.NoticiasResponse;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.persistence.repository.reportes.NoticiasRepository;
import com.sitra.sitra.service.reportes.NoticiasService;
import com.sitra.sitra.service.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class NoticiasServiceImpl implements NoticiasService {

    private final NoticiasRepository noticiasRepository;

    private final WebSocketService webSocketService;

    @Override
    @Transactional
    public NoticiasResponse save(NoticiasRequest request) {
        NoticiasEntity entity = NoticiasRequest.toEntity.apply(request);

        NoticiasEntity saved = noticiasRepository.save(entity);

        NoticiasResponse noticiasResponse = NoticiasResponse.toResponse.apply(saved);

        webSocketService.notificarNuevaNoticia(noticiasResponse);

        return noticiasResponse;
    }

    @Override
    @Transactional
    public NoticiasResponse update(NoticiasRequest request) {
        NoticiasEntity entity = noticiasRepository.getByID(request.getNoticiasId())
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ NOTICIAS ]"));

        NoticiasRequest.toUpdate(request, entity);

        NoticiasEntity updated = noticiasRepository.save(entity);

        NoticiasResponse noticiasResponse = NoticiasResponse.toResponse.apply(updated);

        webSocketService.notificarNuevaEdicionNoticia(noticiasResponse);

        return noticiasResponse;
    }

    @Override
    public List<NoticiasResponse> getAll() {
        List<NoticiasEntity> list = noticiasRepository.getAll();

        return list.stream().map(NoticiasResponse.toResponse).toList();
    }

    @Override
    public List<NoticiasResponse> getAllActives() {
        List<NoticiasEntity> list = noticiasRepository.getAllActives();

        return list.stream().map(NoticiasResponse.toResponse).toList();
    }

    @Override
    public NoticiasResponse getById(Long id) {

        NoticiasEntity entity = noticiasRepository.getByID(id).orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ NOTICIAS ]"));

        return NoticiasResponse.toResponse.apply(entity);
    }

    @Override
    @Transactional
    public NoticiasResponse delete(Long id) {
        NoticiasEntity entity = noticiasRepository.getByID(id).orElseThrow(() -> new NotFoundException("No se encontro la noticia."));

        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        NoticiasEntity deleted = noticiasRepository.save(entity);

        NoticiasResponse noticiasResponse = NoticiasResponse.toResponse.apply(deleted);

        webSocketService.notificarEliminacionNoticia(noticiasResponse);

        return noticiasResponse;
    }
}
