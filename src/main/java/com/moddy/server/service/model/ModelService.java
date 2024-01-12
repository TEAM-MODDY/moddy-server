package com.moddy.server.service.model;


import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.model.dto.request.ModelCreateRequest;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.controller.model.dto.response.*;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.ModelApplyStatus;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.OfferCondition;
import com.moddy.server.domain.prefer_offer_condition.PreferOfferCondition;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import com.moddy.server.domain.prefer_region.PreferRegion;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import com.moddy.server.domain.region.Region;
import com.moddy.server.domain.region.repository.RegionJpaRepository;
import com.moddy.server.domain.user.Gender;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import com.moddy.server.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RegionJpaRepository regionJpaRepository;
    private final KakaoSocialService kakaoSocialService;
    private final AuthService authService;
    private final JwtService jwtService;

    private final String DEFAULT_PROFILE_IMG_URL = "";



    private Page<HairServiceOffer> findOffers(Long userId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC,"id"));
        Page<HairServiceOffer> offerPage = hairServiceOfferJpaRepository.findByUserId(userId, pageRequest);

        return offerPage;
    }

    private ModelApplyStatus calModelStatus(boolean apply, boolean offer){

        if(!apply && !offer) return ModelApplyStatus.NOTHING;
        else if (apply && !offer) return ModelApplyStatus.APPLY;
        else if (apply && offer) return ModelApplyStatus.APPLY_AND_OFFER;
        else throw new NotFoundException(ErrorCode.NOT_FOUND_MODEL_STATUS);

    }

    public ModelMainResponse getModelMainInfo(Long userId, int page, int size){

        User user = modelJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));

        Page<HairServiceOffer> offerPage = findOffers(userId, page, size);

        boolean applyStatus = hairModelApplicationJpaRepository.existsByUserId(userId);
        boolean offerStatus = hairServiceOfferJpaRepository.existsByUserId(userId);
        ModelApplyStatus modelApplyStatus = calModelStatus(applyStatus, offerStatus);

        if(modelApplyStatus != ModelApplyStatus.APPLY_AND_OFFER){
            return new ModelMainResponse(
                    page,
                    size,
                    modelApplyStatus,
                    user.getName(),
                    new ArrayList<>()
            );
        }

        List<OfferResponse> offerResponseList = offerPage.stream().map(offer -> {
            Designer designer = offer.getDesigner();
            List<PreferOfferCondition> preferOfferCondition = preferOfferConditionJpaRepository.findTop2ByHairServiceOfferId(offer.getId());
            List<OfferCondition> offerConditionTop2List = preferOfferCondition.stream().map(PreferOfferCondition::getOfferCondition).collect(Collectors.toList());

            OfferResponse offerResponse = new OfferResponse(
                    offer.getId(),
                    designer.getProfileImgUrl(),
                    designer.getName(),
                    designer.getHairShop().getName(),
                    offerConditionTop2List,
                    offer.getIsClicked()
            );
            return offerResponse;
        }).collect(Collectors.toList());

        return new ModelMainResponse(
                page,
                size,
                modelApplyStatus,
                user.getName(),
                offerResponseList
        );
    }

    private DesignerInfoResponse getDesignerInfoResponseList(HairServiceOffer hairServiceOffer, Long userId, Long offerId){

        Designer designer = designerJpaRepository.findById(hairServiceOffer.getDesigner().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION));

        List<DayOff> dayOffList = dayOffJpaRepository.findAllByDesignerId(designer.getId());
        List<String> dayOfWeekList = dayOffList.stream().map(dayOff -> {
            return dayOff.getDayOfWeek().getValue();
        }).collect(Collectors.toList());

        DesignerInfoResponse designerInfoResponse = new DesignerInfoResponse(
                designer.getProfileImgUrl(),
                designer.getHairShop().getName(),
                designer.getName(),
                designer.getPortfolio().getInstagramUrl(),
                designer.getPortfolio().getNaverPlaceUrl(),
                designer.getIntroduction(),
                designer.getGender(),
                dayOfWeekList,
                designer.getHairShop().getAddress(),
                designer.getHairShop().getDetailAddress()
        );

        return  designerInfoResponse;

    }

    private StyleDetailResponse getStyleDetailResponse(HairServiceOffer hairServiceOffer, Long userId, Long offerId) {

        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(hairServiceOffer.getHairModelApplication().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));

        List<PreferHairStyle> preferHairStyles = preferHairStyleJpaRepository.findAllByHairModelApplicationId(hairServiceOffer.getHairModelApplication().getId());
        List<String> hairStyleList = preferHairStyles.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());

        List<PreferOfferCondition> preferOfferConditionList = preferOfferConditionJpaRepository.findAllByHairServiceOfferId(offerId);
        List<OfferCondition> offerConditionList = preferOfferConditionList.stream().map(PreferOfferCondition::getOfferCondition).collect(Collectors.toList());
        List<Boolean> preferOfferConditionBooleanList = Arrays.stream(OfferCondition.values()).map(condition ->{
            if (offerConditionList.contains(condition)) return true;
            else return false;
        }).collect(Collectors.toList());

        StyleDetailResponse styleDetailResponse = new StyleDetailResponse(
                hairServiceOffer.getIsModelAgree(),
                hairStyleList,
                hairServiceOffer.getOfferDetail(),
                hairModelApplication.getHairDetail(),
                preferOfferConditionBooleanList
        );

        return  styleDetailResponse;
    }

    private void handleOfferClickStatus(HairServiceOffer hairServiceOffer){

        if(!hairServiceOffer.getIsClicked()){
            hairServiceOffer.updateClickStatus();
        }
    }

    @Transactional
    public DetailOfferResponse getModelDetailOfferInfo(Long userId, Long offerId){

        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OFFER_EXCEPTION));

        DesignerInfoResponse designerInfoResponseList = getDesignerInfoResponseList(hairServiceOffer, userId, offerId);
        StyleDetailResponse styleDetailResponse = getStyleDetailResponse(hairServiceOffer, userId, offerId);
        handleOfferClickStatus(hairServiceOffer);

        return new DetailOfferResponse(designerInfoResponseList, styleDetailResponse);
    }

    public OpenChatResponse getOpenChatInfo(Long userId, Long offerId) {

        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUNT_OFFER_EXCEPTION));
        HairModelApplication application = hairModelApplicationJpaRepository.findById(hairServiceOffer.getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        Designer designer = designerJpaRepository.findById(hairServiceOffer.getDesigner().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION));

        DesignerInfoOpenChatResponse designerInfoOpenChatResponse = new DesignerInfoOpenChatResponse(
                designer.getProfileImgUrl(),
                designer.getHairShop().getName(),
                designer.getName(),
                designer.getIntroduction()
        );

        OpenChatResponse openChatResponse = new OpenChatResponse(
                application.getApplicationCaptureUrl(),
                designer.getKakaoOpenChatUrl(),
                designerInfoOpenChatResponse

        );

        return openChatResponse;
    }

    @Transactional
    public void updateOfferAgreeStatus(Long offerId){
        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OFFER_EXCEPTION));

        hairServiceOffer.setIsModelAgree(true);

    }

    @Transactional
    public UserCreateResponse createModel(String baseUrl, String code, ModelCreateRequest request) {

        String kakaoId = kakaoSocialService.getIdFromKakao(baseUrl, code);

        Model model = Model.builder()
                .name(request.name())
                .year(request.year())
                .gender(request.gender())
                .phoneNumber(request.phoneNumber())
                .isMarketingAgree(request.isMarketingAgree())
                .profileImgUrl(DEFAULT_PROFILE_IMG_URL)
                .kakaoId(kakaoId)
                .role(Role.MODEL)
                .build();

        modelJpaRepository.save(model);

        request.preferRegions().stream().forEach(preferRegionId -> {
            Region region = regionJpaRepository.findById(preferRegionId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_REGION_EXCEPTION));
            PreferRegion preferRegion = PreferRegion.builder().user(model).region(region).build();
            preferRegionJpaRepository.save(preferRegion);
        });

        return authService.createUserToken(model.getId().toString());
    }

}
