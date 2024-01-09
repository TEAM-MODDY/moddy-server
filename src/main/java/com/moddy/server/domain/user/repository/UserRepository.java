package com.moddy.server.domain.user.repository;

import com.moddy.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByKakaoId(String kakaoId);
}
