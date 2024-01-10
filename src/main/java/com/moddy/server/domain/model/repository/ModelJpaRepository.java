package com.moddy.server.domain.model.repository;

import com.moddy.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

}

