package com.hydraxon91.backend.repositories.StyleRepository;

import com.hydraxon91.backend.models.StyleModel.StyleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StyleJpaRepository extends JpaRepository<StyleModel, Integer> {
    StyleModel findFirstByOrderById();
}