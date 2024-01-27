package com.moddy.server.service.application;

import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_record.repository.HairServiceRecordJpaRepository;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HairModelApplicationRegisterService {
    private final S3Service s3Service;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final PreferHairStyleJpaRepository preferHairStyleJpaRepository;
    private final HairServiceRecordJpaRepository hairServiceRecordJpaRepository;

    public void deleteModelApplications(final Long modelId) {
        List<HairModelApplication> hairModelApplications = hairModelApplicationJpaRepository.findAllByModelId(modelId);
        hairModelApplications.forEach(hairModelApplication -> {
            deleteApplicationImage(hairModelApplication);
            preferHairStyleJpaRepository.deleteAllByHairModelApplication(hairModelApplication);
            hairServiceRecordJpaRepository.deleteAllByHairModelApplication(hairModelApplication);
            hairModelApplicationJpaRepository.deleteById(hairModelApplication.getId());
        });
    }

    private void deleteApplicationImage(final HairModelApplication hairModelApplication) {
        s3Service.deleteS3Image(hairModelApplication.getApplicationCaptureUrl());
        s3Service.deleteS3Image(hairModelApplication.getModelImgUrl());
    }
}
