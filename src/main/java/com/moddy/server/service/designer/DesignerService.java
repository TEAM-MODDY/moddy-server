package com.moddy.server.service.designer;

import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.DesignerCreateResponse;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import com.moddy.server.external.s3.S3Service;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private Page<HairModelApplication> findApplications(Long userId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC,"id"));
        Page<HairModelApplication> applicationPage = hairModelApplicationJpaRepository.findByUserId(userId, pageRequest);
        return applicationPage;
    }
    @Transactional
    public DesignerCreateResponse createDesigner(String baseUrl,String code, DesignerCreateRequest request) {

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

        TokenPair tokenPair = jwtService.generateTokenPair(kakaoId);
        DesignerCreateResponse designerCreateResponse = new DesignerCreateResponse(tokenPair.accessToken(), tokenPair.refreshToken());
        return designerCreateResponse;
    }

    @Transactional
    public DesignerMainResponse getMainView(Long userId, int page, int size){
        List<HairModelApplication> hairModelApplications= hairModelApplicationJpaRepository.findAllByUserId(userId);
        User user = designerJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));



    }
}
