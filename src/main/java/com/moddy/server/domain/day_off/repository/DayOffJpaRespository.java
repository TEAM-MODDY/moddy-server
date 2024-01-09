package com.moddy.server.domain.day_off.repository;

import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.day_off.DayOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayOffJpaRespository extends JpaRepository<DayOff, Long> {

    List<DayOff> findByUserId(Long userId);
}
