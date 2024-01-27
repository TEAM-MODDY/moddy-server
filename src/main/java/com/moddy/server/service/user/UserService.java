package com.moddy.server.service.user;

import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.user.dto.response.UserDetailResponseDto;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.hair_service_record.repository.HairServiceRecordJpaRepository;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.external.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.moddy.server.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;
import static com.moddy.server.domain.user.Role.MODEL;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;
    private final PreferHairStyleJpaRepository preferHairStyleJpaRepository;
    private final HairServiceRecordJpaRepository hairServiceRecordJpaRepository;
    private final DayOffJpaRepository dayOffJpaRepository;
    private final PreferRegionJpaRepository preferRegionJpaRepository;
    private final ModelJpaRepository modelJpaRepository;
    private final DesignerJpaRepository designerJpaRepository;
    private final S3Service s3Service;

    private void deleteModelInfos(Long userId) {
        deleteModelHairServiceOfferInfos(userId);
        deleteModelApplications(userId);
        deleteModelPreferRegions(userId);
        deleteModelInfo(userId);
    }

    private void deleteModelHairServiceOfferInfos(Long userId) {
        List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByModelId(userId);
        hairServiceOffers.forEach(hairServiceOffer -> {
            preferOfferConditionJpaRepository.deleteAllByHairServiceOffer(hairServiceOffer);
            hairServiceOfferJpaRepository.deleteById(hairServiceOffer.getId());
        });
    }

    private void deleteModelApplications(Long userId) {
        List<HairModelApplication> hairModelApplications = hairModelApplicationJpaRepository.findAllByModelId(userId);
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

    private void deleteModelPreferRegions(Long userId) {
        preferRegionJpaRepository.deleteAllByModelId(userId);
    }

    private void deleteModelInfo(Long userId) {
        modelJpaRepository.deleteById(userId);
        userRepository.deleteById(userId);
    }

    private void deleteDesignerInfos(final User user) {
        deleteDesignerHairServiceOfferInfos(user.getId());
        deleteDesignerDayOffs(user.getId());
        deleteDesignerProfileImage(user);
        deleteDesignerInfo(user.getId());
    }

    private void deleteDesignerHairServiceOfferInfos(Long userId) {
        List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByDesignerId(userId);
        hairServiceOffers.forEach(hairServiceOffer -> {
            preferOfferConditionJpaRepository.deleteAllByHairServiceOffer(hairServiceOffer);
            hairServiceOfferJpaRepository.deleteById(hairServiceOffer.getId());
        });
    }

    private void deleteDesignerDayOffs(Long userId) {
        dayOffJpaRepository.deleteAllByDesignerId(userId);
    }

    private void deleteDesignerInfo(Long userId) {
        designerJpaRepository.deleteById(userId);
        userRepository.deleteById(userId);
    }

    private void deleteDesignerProfileImage(final User user) {
        s3Service.deleteS3Image(user.getProfileImgUrl());
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        if (user.getRole() == MODEL) deleteModelInfos(userId);
        else deleteDesignerInfos(user);
    }
}
