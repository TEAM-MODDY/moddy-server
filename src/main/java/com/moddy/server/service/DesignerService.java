package com.moddy.server.service;

import com.moddy.server.controller.auth.dto.request.SocialLoginRequest;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.DesignerCreateResponse;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Builder
public class DesignerService {
    private final DesignerJpaRepository designerJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final S3Service s3Service;
    private final KakaoSocialService kakaoSocialService;
    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;



    @Transactional
    public DesignerCreateResponse createDesigner(DesignerCreateRequest request, SocialLoginRequest socialLoginRequest){


        String profileImgUrl = s3Service.uploadProfileImage(request.profileImg(), Role.HAIR_DESIGNER);
        String kakaoId = String.valueOf(kakaoSocialService.login(socialLoginRequest));

        Designer designer = Designer.builder()
                .hairShop(request.hairShop())
                .portfolio(request.portfolio())
                .introduction(request.introduction())
                .kakaoOpenChatUrl(request.kakaoOpenChatUrl())
                .build();

        User user = User.builder()
                .kakaoId(kakaoId)
                .name(request.name())
                .year(request.year())
                .build();



    }



}
