package com.moddy.server.service.model;


import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.request.ModelApplicationRequest;
import com.moddy.server.controller.model.dto.response.ApplicationUserDetailResponse;
import com.moddy.server.controller.model.dto.response.DesignerInfoResponse;
import com.moddy.server.controller.model.dto.response.DetailOfferResponse;
import com.moddy.server.controller.model.dto.response.ModelMainResponse;
import com.moddy.server.controller.model.dto.response.OfferResponse;
import com.moddy.server.controller.model.dto.response.StyleDetailResponse;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
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
import com.moddy.server.domain.prefer_offer_condition.OfferCondition;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelJpaRepository modelJpaRepository;
    private final DesignerJpaRepository designerJpaRepository;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;
    private final DayOffJpaRepository dayOffJpaRepository;
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
    public DetailOfferResponse getOfferDetail(Long userId, Long offerId) {

        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OFFER_EXCEPTION));

        DesignerInfoResponse designerInfoResponseList = getDesignerInfoResponse(hairServiceOffer, userId, offerId);
        StyleDetailResponse styleDetailResponse = getStyleDetailResponse(hairServiceOffer, userId, offerId);
        handleOfferClickStatus(hairServiceOffer);

        return new DetailOfferResponse(designerInfoResponseList, styleDetailResponse);
    }

    public ApplicationUserDetailResponse getUserDetailInApplication(final Long userId) {
        Model model = modelJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        List<String> preferRegions = preferRegionJpaRepository.findAllByModelId(model.getId())
                .stream()
                .map(p -> p.getRegion().getName())
                .toList();

        return new ApplicationUserDetailResponse(model.getName(), model.getGender().getValue(), model.getAge(), preferRegions);
    }

    private DesignerInfoResponse getDesignerInfoResponse(HairServiceOffer hairServiceOffer, Long userId, Long offerId) {

        Designer designer = designerJpaRepository.findById(hairServiceOffer.getDesigner().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION));

        List<DayOff> dayOffList = dayOffJpaRepository.findAllByDesignerId(designer.getId());
        List<String> dayOfWeekList = dayOffList.stream().map(dayOff -> {
            return dayOff.getDayOfWeek().getValue();
        }).collect(Collectors.toList());

        DesignerInfoResponse designerInfoResponse = new DesignerInfoResponse(designer.getProfileImgUrl(), designer.getHairShop().getName(), designer.getName(), designer.getPortfolio().getInstagramUrl(), designer.getPortfolio().getNaverPlaceUrl(), designer.getIntroduction(), designer.getGender().getValue(), dayOfWeekList, designer.getHairShop().getAddress(), designer.getHairShop().getDetailAddress());

        return designerInfoResponse;

    }

    private StyleDetailResponse getStyleDetailResponse(HairServiceOffer hairServiceOffer, Long userId, Long offerId) {

        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(hairServiceOffer.getHairModelApplication().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));

        List<PreferHairStyle> preferHairStyles = preferHairStyleJpaRepository.findAllByHairModelApplicationId(hairServiceOffer.getHairModelApplication().getId());
        List<String> hairStyleList = preferHairStyles.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());

        List<PreferOfferCondition> preferOfferConditionList = preferOfferConditionJpaRepository.findAllByHairServiceOfferId(offerId);
        List<OfferCondition> offerConditionList = preferOfferConditionList.stream().map(PreferOfferCondition::getOfferCondition).collect(Collectors.toList());
        List<Boolean> preferOfferConditionBooleanList = Arrays.stream(OfferCondition.values()).map(condition -> {
            if (offerConditionList.contains(condition)) return true;
            else return false;
        }).collect(Collectors.toList());

        StyleDetailResponse styleDetailResponse = new StyleDetailResponse(hairServiceOffer.getIsModelAgree(), hairStyleList, hairServiceOffer.getOfferDetail(), hairModelApplication.getHairDetail(), preferOfferConditionBooleanList);

        return styleDetailResponse;
    }

    private void handleOfferClickStatus(HairServiceOffer hairServiceOffer) {

        if (!hairServiceOffer.getIsClicked()) {
            hairServiceOffer.updateClickStatus();
        }
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
