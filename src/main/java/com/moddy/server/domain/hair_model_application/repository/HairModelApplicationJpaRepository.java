package com.moddy.server.domain.hair_model_application.repository;

import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HairModelApplicationJpaRepository extends JpaRepository<HairModelApplication, Long> {

    Boolean existsByUserId(Long userId);

}
