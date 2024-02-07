package com.moddy.server.service.designer;

import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.common.util.SmsUtil;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.designer.dto.response.DownloadUrlResponseDto;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.external.kakao.feign.KakaoApiClient;
import com.moddy.server.external.kakao.feign.KakaoAuthApiClient;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import com.moddy.server.external.s3.S3Service;
import com.moddy.server.service.auth.AuthService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuthService authService;
    private final PreferRegionJpaRepository preferRegionJpaRepository;
    private final UserRepository userRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final SmsUtil smsUtil;

    public DownloadUrlResponseDto getOfferImageDownloadUrl(final Long userId, final String offerImageUrl) {
        Designer designer = designerJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND_EXCEPTION));
        String preSignedUrl = s3Service.getPreSignedUrlToDownload(offerImageUrl);
        return new DownloadUrlResponseDto(preSignedUrl);
    }
}
