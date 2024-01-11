package com.moddy.server.domain.hair_service_record.repository;

import com.moddy.server.domain.hair_service_record.HairServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HairServcieRecordJpaRepository extends JpaRepository<HairServiceRecord, Long> {
    List<HairServiceRecord> findAllByHairModelApplicationId(Long applicationId);

}
