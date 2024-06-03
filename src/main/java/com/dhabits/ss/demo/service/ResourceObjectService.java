package com.dhabits.ss.demo.service;

import com.dhabits.ss.demo.domain.entity.ResourceObjectEntity;
import com.dhabits.ss.demo.domain.model.ResourceObject;
import com.dhabits.ss.demo.repository.ResourceObjectRepository;
import lombok.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class ResourceObjectService {

    private final ResourceObjectRepository repository;

    public Integer save(ResourceObject resourceObject) {
        return repository.save(new ResourceObjectEntity(
                resourceObject.getId(), resourceObject.getValue(),
                resourceObject.getPath())).getId();

    }

    public ResourceObject get(int id) {
        return repository.findById(id)
                .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()))
                .orElse(null);
    }

}
