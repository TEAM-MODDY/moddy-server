package com.moddy.server.repository;


import com.moddy.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialId(Long socialId);
}