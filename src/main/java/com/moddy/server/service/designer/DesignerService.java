package com.moddy.server.service.designer;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.ConflictException;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.common.util.SmsUtil;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.request.OfferCreateRequest;
import com.moddy.server.controller.designer.dto.response.ApplicationDetailInfoResponse;
import com.moddy.server.controller.designer.dto.response.ApplicationInfoResponse;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.DownloadUrlResponseDto;
import com.moddy.server.controller.designer.dto.response.HairModelApplicationResponse;
import com.moddy.server.controller.designer.dto.response.HairRecordResponse;
import com.moddy.server.controller.designer.dto.response.ModelInfoResponse;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.hair_service_record.HairServiceRecord;
import com.moddy.server.domain.hair_service_record.repository.HairServcieRecordJpaRepository;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.PreferOfferCondition;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import com.moddy.server.domain.prefer_region.PreferRegion;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import com.moddy.server.external.s3.S3Service;
import com.moddy.server.service.auth.AuthService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.moddy.server.common.exception.enums.ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Builder
public class DesignerService {
    private final DesignerJpaRepository designerJpaRepository;
    private final DayOffJpaRepository dayOffJpaRepository;
    private final S3Service s3Service;
    private final KakaoSocialService kakaoSocialService;
    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;
    private final JwtService jwtService;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final PreferHairStyleJpaRepository preferHairStyleJpaRepository;
    private final ModelJpaRepository modelJpaRepository;
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;
    private final HairServcieRecordJpaRepository hairServiceRecordJpaRepository;
    private final AuthService authService;
    private final PreferRegionJpaRepository preferRegionJpaRepository;
    private final HairServcieRecordJpaRepository hairServcieRecordJpaRepository;
    private final UserRepository userRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final SmsUtil smsUtil;


    @Transactional
    public DesignerMainResponse getDesignerMainInfo(Long userId, int page, int size) {
        Designer designer = designerJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION));

        Page<HairModelApplication> applicationPage = findApplicationsByPaging(page, size);
        long totalElements = applicationPage.getTotalElements();

        List<HairModelApplicationResponse> applicationResponsesList = applicationPage.stream().map(application -> {

            Model model = modelJpaRepository.findById(application.getModel().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.MODEL_NOT_FOUND_EXCEPTION));

            List<PreferHairStyle> preferHairStyle = preferHairStyleJpaRepository.findTop2ByHairModelApplicationId(application.getId());

            List<String> top2hairStyles = preferHairStyle.stream().map(haireStyle -> {
                return haireStyle.getHairStyle().getValue();
            }).collect(Collectors.toList());

            HairModelApplicationResponse applicationResponse = new HairModelApplicationResponse(
                    application.getId(),
                    model.getName(),
                    model.getAge(),
                    application.getModelImgUrl(),
                    model.getGender().getValue(),
                    top2hairStyles
            );
            return applicationResponse;
        }).collect(Collectors.toList());

        return new DesignerMainResponse(
                page,
                size,
                totalElements,
                designer.getName(),
                applicationResponsesList
        );
    }

    @Transactional
    public UserCreateResponse createDesigner(Long userId, DesignerCreateRequest request, MultipartFile profileImg) {

        String profileImgUrl = s3Service.uploadProfileImage(profileImg, Role.HAIR_DESIGNER);

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION));
        if (designerJpaRepository.existsById(userId))
            throw new ConflictException(ErrorCode.ALREADY_EXIST_USER_EXCEPTION);
        user.update(request.name(), request.gender(), request.phoneNumber(), request.isMarketingAgree(), profileImgUrl, Role.HAIR_DESIGNER);

        HairShop hairShop = HairShop.builder()
                .name(request.hairShop().name())
                .address(request.hairShop().address())
                .detailAddress(request.hairShop().detailAddress())
                .build();
        Portfolio portfolio = Portfolio.builder()
                .instagramUrl(request.portfolio().instagramUrl())
                .naverPlaceUrl(request.portfolio().naverPlaceUrl())
                .build();
        designerJpaRepository.designerRegister(user.getId(), hairShop.getAddress(), hairShop.getDetailAddress(), hairShop.getName(), portfolio.getInstagramUrl(), portfolio.getNaverPlaceUrl(), request.introduction(), request.kakaoOpenChatUrl());
        Designer designer = designerJpaRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND_EXCEPTION));
        request.dayOffs().stream()
                .forEach(d -> {
                    DayOff dayOff = DayOff.builder()
                            .dayOfWeek(d)
                            .designer(designer)
                            .build();
                    dayOffJpaRepository.save(dayOff);

                });
        return authService.createUserToken(designer.getId().toString());
    }


    @Transactional
    public void postOffer(Long userId, Long applicationId, OfferCreateRequest request) throws IOException {
        Designer designer = designerJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND_EXCEPTION));
        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        if (hairServiceOfferJpaRepository.existsByHairModelApplicationId(applicationId))
            throw new ConflictException(ErrorCode.ALREADY_EXIST_OFFER_EXCEPTION);
        HairServiceOffer offer = HairServiceOffer.builder()
                .model(hairModelApplication.getModel())
                .hairModelApplication(hairModelApplication)
                .designer(designer)
                .offerDetail(request.offerDetail())
                .isModelAgree(false)
                .isClicked(false)
                .build();
        hairServiceOfferJpaRepository.save(offer);

        request.preferOfferConditions().stream()
                .forEach(p -> {
                    PreferOfferCondition preferOfferCondition = PreferOfferCondition.builder()
                            .offerCondition(p)
                            .hairServiceOffer(offer)
                            .build();
                    preferOfferConditionJpaRepository.save(preferOfferCondition);

                });

        final String modelName = hairModelApplication.getModel().getName();
        final String modelPhoneNumber = hairModelApplication.getModel().getPhoneNumber();
        smsUtil.sendOfferToModel(modelPhoneNumber, modelName);
    }

    @Transactional
    public ApplicationDetailInfoResponse getApplicationDetail(Long userId, Long applicationId) {

        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));

        Model model = modelJpaRepository.findById(hairModelApplication.getModel().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));

        List<PreferHairStyle> preferHairStyles = preferHairStyleJpaRepository.findAllByHairModelApplicationId(applicationId);

        List<String> preferhairStyleList = preferHairStyles.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());

        List<HairServiceRecord> hairServiceRecords = hairServiceRecordJpaRepository.findAllByHairModelApplicationId(applicationId);
        hairServiceRecords.sort(Comparator.comparingInt(e -> e.getServiceRecordTerm().ordinal()));

        List<PreferRegion> preferRegions = preferRegionJpaRepository.findAllByModelId(model.getId());

        List<String> regionList = preferRegions.stream().map(preferregion -> {
            return preferregion.getRegion().getName();
        }).collect(Collectors.toList());

        List<HairRecordResponse> recordResponseList = hairServiceRecords.stream().map(records -> {
            HairRecordResponse hairRecordResponse = new HairRecordResponse(
                    records.getServiceRecordTerm().getValue(),
                    records.getServiceRecord().getValue()
            );
            return hairRecordResponse;
        }).collect(Collectors.toList());

        ApplicationInfoResponse applicationInfoResponse = new ApplicationInfoResponse(
                applicationId,
                hairModelApplication.getModelImgUrl(),
                hairModelApplication.getHairLength().getValue(),
                preferhairStyleList,
                recordResponseList,
                hairModelApplication.getHairDetail(),
                getIsSendStatus(applicationId, model.getId())
        );

        ModelInfoResponse modelInfoResponse = new ModelInfoResponse(
                model.getId(),
                model.getName(),
                model.getAge(),
                model.getGender().getValue(),
                regionList,
                hairModelApplication.getInstagramId()
        );

        return new ApplicationDetailInfoResponse(
                applicationInfoResponse,
                modelInfoResponse
        );
    }

    private Page<HairModelApplication> findApplicationsByPaging(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<HairModelApplication> applicationPage = hairModelApplicationJpaRepository.findAll(pageRequest);

        return applicationPage;
    }

    private Boolean getIsSendStatus(Long applicationId, Long userId) {
        Optional<HairServiceOffer> offer = hairServiceOfferJpaRepository.findByHairModelApplicationIdAndModelId(applicationId, userId);
        return offer.isPresent();
    }

    public DownloadUrlResponseDto getOfferImageDownloadUrl(final Long userId, final String offerImageUrl) {
        //Designer designer = designerJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND_EXCEPTION));
        String s3Key = offerImageUrl.substring(54);
        String preSignedUrl = s3Service.getPreSignedUrlToDownload(s3Key);
        return new DownloadUrlResponseDto(preSignedUrl);
    }
}
