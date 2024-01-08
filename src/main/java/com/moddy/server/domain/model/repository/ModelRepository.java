package com.moddy.server.domain.model.repository;

import com.moddy.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<User, Long> {

}
