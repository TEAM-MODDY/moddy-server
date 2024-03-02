package com.moddy.server.service.application;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.request.ModelApplicationRequest;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_record.HairServiceRecord;
import com.moddy.server.domain.hair_service_record.repository.HairServiceRecordJpaRepository;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HairModelApplicationRegisterService {
    private final S3Service s3Service;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final PreferHairStyleJpaRepository preferHairStyleJpaRepository;
    private final HairServiceRecordJpaRepository hairServiceRecordJpaRepository;
    private final ModelJpaRepository modelJpaRepository;

    @Transactional
    public void postApplication(final Long modelId, final MultipartFile modelImgUrl, final MultipartFile applicationCaptureImgUrl, final ModelApplicationRequest applicationInfo) {
        Model model = modelJpaRepository.findById(modelId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        String s3ModelImgUrl = s3Service.uploadProfileImage(modelImgUrl, model.getRole());
        String s3applicationCaptureImgUrl = s3Service.uploadApplicationImage(applicationCaptureImgUrl);

        HairModelApplication hairModelApplication = new HairModelApplication(model,applicationInfo.hairLength(),applicationInfo.hairDetail(),s3ModelImgUrl,applicationInfo.instagramId(),s3applicationCaptureImgUrl);
        hairModelApplicationJpaRepository.save(hairModelApplication);

        savePreferHairStyles(applicationInfo, hairModelApplication);
        saveHairServiceRecords(applicationInfo, hairModelApplication);
    }

    @Transactional
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

    private void savePreferHairStyles(final ModelApplicationRequest applicationInfo, final HairModelApplication hairModelApplication){
        applicationInfo.preferHairStyles().stream().forEach(hairStyle -> {
            PreferHairStyle preferHairStyle = new PreferHairStyle(hairModelApplication, hairStyle);
            preferHairStyleJpaRepository.save(preferHairStyle);
        });
    }

    private void saveHairServiceRecords(final ModelApplicationRequest applicationInfo, final HairModelApplication hairModelApplication){
        applicationInfo.getHairServiceRecords().stream().forEach(modelHairServiceRecord -> {
            HairServiceRecord hairServiceRecord = new HairServiceRecord(hairModelApplication,modelHairServiceRecord.hairService(), modelHairServiceRecord.hairServiceTerm());
            hairServiceRecordJpaRepository.save(hairServiceRecord);
        });
    }
}
