package com.moddy.server.domain.prefer_region.repository;

import com.moddy.server.domain.prefer_region.PreferRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferRegionJpaRepository extends JpaRepository<PreferRegion, Long> {
    List<PreferRegion> findAllByUserId(Long userId);

}
