package com.sitra.sitra.service.maestros.impl;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import com.sitra.sitra.repository.maestros.TablaMaestraRepository;
import com.sitra.sitra.service.maestros.TablaMaestraService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TablaMaestraServiceImpl implements TablaMaestraService {

    private final TablaMaestraRepository tablaMaestraRepository;

    @Override
    public TablaMaestraEntity save(TablaMaestraEntity entity) {

        TablaMaestraEntity entitySave = tablaMaestraRepository.save(entity);

        return entitySave;
    }
}
