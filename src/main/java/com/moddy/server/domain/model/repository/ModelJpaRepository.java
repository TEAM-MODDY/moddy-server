package com.moddy.server.domain.model.repository;

import com.moddy.server.domain.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelJpaRepository extends JpaRepository<Model, Long> {
    Optional<Model> findById(Long userId);

}

