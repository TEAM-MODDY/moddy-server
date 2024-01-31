package com.moddy.server.service.model;


import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.request.ModelApplicationRequest;
import com.moddy.server.controller.model.dto.response.ApplicationUserDetailResponse;
import com.moddy.server.controller.model.dto.response.ModelMainResponse;
import com.moddy.server.controller.model.dto.response.OfferResponse;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.hair_service_record.HairServiceRecord;
import com.moddy.server.domain.hair_service_record.repository.HairServiceRecordJpaRepository;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.ModelApplyStatus;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.PreferOfferCondition;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import com.moddy.server.domain.user.User;
import com.moddy.server.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelJpaRepository modelJpaRepository;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;
    private final PreferHairStyleJpaRepository preferHairStyleJpaRepository;
    private final PreferRegionJpaRepository preferRegionJpaRepository;
    private final HairServiceRecordJpaRepository hairServiceRecordJpaRepository;
    private final S3Service s3Service;


    public ModelMainResponse getModelMainInfo(Long userId, int page, int size) {

        User user = modelJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));

        Page<HairServiceOffer> offerPage = findOffersByPaging(userId, page, size);
        long totalElements = offerPage.getTotalElements();

        boolean applyStatus = hairModelApplicationJpaRepository.existsByModelId(userId);
        boolean offerStatus = hairServiceOfferJpaRepository.existsByModelId(userId);
        ModelApplyStatus modelApplyStatus = calModelStatus(applyStatus, offerStatus);

        if (modelApplyStatus != ModelApplyStatus.APPLY_AND_OFFER) {
            return new ModelMainResponse(page, size, totalElements, modelApplyStatus, user.getName(), new ArrayList<>());
        }

        List<OfferResponse> offerResponseList = offerPage.stream().map(offer -> {
            Designer designer = offer.getDesigner();
            List<PreferOfferCondition> preferOfferCondition = preferOfferConditionJpaRepository.findTop2ByHairServiceOfferId(offer.getId());
            List<String> offerConditionTop2List = preferOfferCondition.stream().map(offerCondition -> {
                return offerCondition.getOfferCondition().getValue();
            }).collect(Collectors.toList());

            OfferResponse offerResponse = new OfferResponse(offer.getId(), designer.getProfileImgUrl(), designer.getName(), designer.getHairShop().getName(), offerConditionTop2List, offer.getIsClicked());
            return offerResponse;
        }).collect(Collectors.toList());

        return new ModelMainResponse(page, size, totalElements, modelApplyStatus, user.getName(), offerResponseList);
    }

    @Transactional
    public void postApplication(Long userId, MultipartFile modelImgUrl, MultipartFile applicationCaptureImgUrl, ModelApplicationRequest applicationInfo) {

        Model model = modelJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        String s3ModelImgUrl = s3Service.uploadProfileImage(modelImgUrl, model.getRole());
        String s3applicationCaptureImgUrl = s3Service.uploadApplicationImage(applicationCaptureImgUrl);

        HairModelApplication hairModelApplication = HairModelApplication.builder().model(model).hairLength(applicationInfo.hairLength()).hairDetail(applicationInfo.hairDetail()).modelImgUrl(s3ModelImgUrl).instagramId(applicationInfo.instagramId()).applicationCaptureUrl(s3applicationCaptureImgUrl).build();

        hairModelApplicationJpaRepository.save(hairModelApplication);

        applicationInfo.preferHairStyles().stream().forEach(hairStyle -> {
            PreferHairStyle preferHairStyle = PreferHairStyle.builder().hairModelApplication(hairModelApplication).hairStyle(hairStyle).build();
            preferHairStyleJpaRepository.save(preferHairStyle);
        });

        applicationInfo.getHairServiceRecords().stream().forEach(modelHairServiceRecord -> {
            HairServiceRecord hairServiceRecord = HairServiceRecord.builder().hairModelApplication(hairModelApplication).serviceRecord(modelHairServiceRecord.hairService()).serviceRecordTerm(modelHairServiceRecord.hairServiceTerm()).build();
            hairServiceRecordJpaRepository.save(hairServiceRecord);
        });

    }

    @Transactional
    public void updateOfferAgreeStatus(Long offerId) {
        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OFFER_EXCEPTION));

        hairServiceOffer.setIsModelAgree(true);
    }

    public ApplicationUserDetailResponse getUserDetailInApplication(final Long userId) {
        Model model = modelJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        List<String> preferRegions = preferRegionJpaRepository.findAllByModelId(model.getId())
                .stream()
                .map(p -> p.getRegion().getName())
                .toList();

        return new ApplicationUserDetailResponse(model.getName(), model.getGender().getValue(), model.getAge(), preferRegions);
    }

    private Page<HairServiceOffer> findOffersByPaging(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<HairServiceOffer> offerPage = hairServiceOfferJpaRepository.findByModelId(userId, pageRequest);

        return offerPage;
    }

    private ModelApplyStatus calModelStatus(boolean apply, boolean offer) {

        if (!apply && !offer) return ModelApplyStatus.NOTHING;
        else if (apply && !offer) return ModelApplyStatus.APPLY;
        else if (apply && offer) return ModelApplyStatus.APPLY_AND_OFFER;
        else throw new NotFoundException(ErrorCode.NOT_FOUND_MODEL_STATUS);

    }

}
