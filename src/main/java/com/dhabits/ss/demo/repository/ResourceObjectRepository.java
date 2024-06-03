package com.dhabits.ss.demo.repository;

import com.dhabits.ss.demo.domain.entity.ResourceObjectEntity;
import org.springframework.data.jpa.repository.*;

public interface ResourceObjectRepository extends JpaRepository<ResourceObjectEntity, Integer> {
}
