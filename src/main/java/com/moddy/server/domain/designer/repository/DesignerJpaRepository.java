package com.moddy.server.domain.designer.repository;

import com.moddy.server.domain.designer.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignerJpaRepository extends JpaRepository<Designer, Long> {

}