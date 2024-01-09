package com.moddy.server.service;

import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.auth.dto.request.SocialLoginRequest;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.DesignerCreateResponse;
import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.user.Gender;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserJpaRepository;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import com.moddy.server.external.s3.S3Service;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Builder
public class DesignerService {
    private final DesignerJpaRepository designerJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final DayOffJpaRepository dayOffJpaRepository;
    private final S3Service s3Service;
    private final KakaoSocialService kakaoSocialService;
    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;
    private final JwtService jwtService;


    @Transactional
    public DesignerCreateResponse createDesigner(String code, DesignerCreateRequest request) {

        String profileImgUrl = s3Service.uploadProfileImage(request.profileImg(), Role.HAIR_DESIGNER);
//        String profileImgUrl = "a";

//        String kakaoId = String.valueOf(kakaoSocialService.login(SocialLoginRequest.of(code)));
        String kakaoId = "a";

        Designer designer = Designer.builder()
                .hairShop(request.hairShop())
                .portfolio(request.portfolio())
                .introduction(request.introduction())
                .kakaoOpenChatUrl(request.kakaoOpenChatUrl())
                .kakaoId(kakaoId)
                .name(request.name())
                .gender(Gender.valueOf(request.gender()))
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

        TokenPair tokenPair = jwtService.generateTokenPair(kakaoId);
        DesignerCreateResponse designerCreateResponse = new DesignerCreateResponse(tokenPair.accessToken(), tokenPair.refreshToken());
        return designerCreateResponse;




    }


}
