package com.moddy.server.domain.region.repository;

import com.moddy.server.domain.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionJpaRepository extends JpaRepository<Region, Long> {
}
