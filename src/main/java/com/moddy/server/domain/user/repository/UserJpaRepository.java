package com.moddy.server.domain.user.repository;

import com.moddy.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
