package com.moddy.server.domain.model.repository;

import com.moddy.server.domain.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelJpaRepository extends JpaRepository<Model, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into model (id, year) VALUES (:id, :year)", nativeQuery = true)
    void modelRegister(@Param("id") Long id, @Param("year") String year);

}

