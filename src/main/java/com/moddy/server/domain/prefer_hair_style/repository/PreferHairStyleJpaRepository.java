package com.moddy.server.domain.prefer_hair_style.repository;

import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferHairStyleJpaRepository extends JpaRepository<PreferHairStyle, Long> {

    List<PreferHairStyle> findTop2ByHairModelApplicationId(Long applicationId);
    List<PreferHairStyle> findAllByHairModelApplicationId(Long applicationId);

    void deleteAllByHairModelApplication(HairModelApplication hairModelApplication);
}
