package com.moddy.server.service.designer;

import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.user.Role;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import com.moddy.server.external.s3.S3Service;
import com.moddy.server.service.auth.AuthService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuthService authService;

    @Transactional
    public UserCreateResponse createDesigner(String baseUrl, String code, DesignerCreateRequest request) {

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

        return authService.createUserToken(designer.getId().toString());
    }
}
