package com.moddy.server.domain.hair_service_record.repository;

import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_service_record.HairServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HairServiceRecordJpaRepository extends JpaRepository<HairServiceRecord, Long> {
    void deleteAllByHairModelApplication(HairModelApplication hairModelApplication);
}
