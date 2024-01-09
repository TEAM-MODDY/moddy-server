package com.moddy.server.domain.day_off.repository;

import com.moddy.server.domain.day_off.DayOff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayOffJpaRepository extends JpaRepository<DayOff, Long> {
}
