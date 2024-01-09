package com.moddy.server.domain.designer.repository;

import com.moddy.server.domain.designer.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DesignerJpaRepository extends JpaRepository<Designer, Long> {

    Optional<Designer> findByUserId(Long userId);
}
