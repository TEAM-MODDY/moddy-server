package com.moddy.server.service.designer;

import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.DesignerCreateResponse;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.HairModelApplicationResponse;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_hair_style.HairStyle;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import com.moddy.server.external.s3.S3Service;
import com.moddy.server.service.user.UserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private Page<HairModelApplication> findApplications(int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC,"id"));
        Page<HairModelApplication> applicationPage = hairModelApplicationJpaRepository.findAll(pageRequest);
        return applicationPage;
    }

    @Transactional
    public DesignerCreateResponse createDesigner(String baseUrl, String code, DesignerCreateRequest request) {

        String profileImgUrl = s3Service.uploadProfileImage(request.profileImg(), Role.HAIR_DESIGNER);

        String kakaoId = kakaoSocialService.getIdFromKakao(baseUrl, code);

        HairShop hairShop = HairShop.builder()
                .name(request.hairShopName())
                .address(request.hairShopAddress())
                .detailAddress(request.hairShopAddressDetail())
                .build();
        Portfolio portfolio = Portfolio.builder()
                .instagramUrl(request.instagramUrl())
                .naverPlaceUrl(request.naverPlaceUrl())
                .build();
        Designer designer = Designer.builder()
                .hairShop(hairShop)
                .portfolio(portfolio)
                .introduction(request.introduction())
                .kakaoOpenChatUrl(request.kakaoOpenChatUrl())
                .kakaoId(kakaoId)
                .name(request.name())
                .gender(request.gender())
                .phoneNumber(request.phoneNumber())
                .isMarketingAgree(request.isMarketingAgree())
                .profileImgUrl(profileImgUrl)
                .role(Role.HAIR_DESIGNER)
                .build();
        designerJpaRepository.save(designer);
        request.dayOffs().stream()
                .forEach(d -> {
                    DayOff dayOff = DayOff.builder()
                            .dayOfWeek(d)
                            .designer(designer)
                            .build();
                    dayOffJpaRepository.save(dayOff);

                });
        TokenPair tokenPair = jwtService.generateTokenPair(designer.getId().toString());
        DesignerCreateResponse designerCreateResponse = new DesignerCreateResponse(tokenPair.accessToken(), tokenPair.refreshToken());
        return designerCreateResponse;
    }

    @Transactional
    public DesignerMainResponse getDesignerMainView(Long userId, int page, int size){
        User user = new User();
        Designer designer = designerJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION));

        Page<HairModelApplication> applicationPage = findApplications(page, size);

        List<HairModelApplicationResponse> applicationResponsesList = applicationPage.stream().map(application -> {

            Model model = modelJpaRepository.findById(application.getUser().getId()).orElseThrow(() -> new NotFoundException(ErrorCode.MODEL_NOT_FOUND_EXCEPTION));

            List<PreferHairStyle> preferHairStyle = preferHairStyleJpaRepository.findTop2ByHairModelApplicationId(application.getId());

            List<HairStyle> top2hairStyles= preferHairStyle.stream().map(PreferHairStyle::getHairStyle).collect(Collectors.toList());
            HairModelApplicationResponse applicationResponse = new HairModelApplicationResponse(
                    application.getId(),
                    model.getName(),
                    user.getAge(model.getYear()),
                    model.getProfileImgUrl(),
                    model.getGender().getValue(),
                    top2hairStyles
            );
            return applicationResponse;
        }).collect(Collectors.toList());

        return new DesignerMainResponse(
                page,
                size,
                designer.getName(),
                applicationResponsesList
        );
    }
}
