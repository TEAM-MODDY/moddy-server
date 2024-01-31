package com.moddy.server.service.designer;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.ConflictException;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.external.s3.S3Service;
import com.moddy.server.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.moddy.server.common.exception.enums.ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class DesignerRegisterService {
    private final DesignerJpaRepository designerJpaRepository;
    private final DayOffJpaRepository dayOffJpaRepository;
    private final S3Service s3Service;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Transactional
    public UserCreateResponse createDesigner(Long designerId, DesignerCreateRequest request, MultipartFile profileImg) {

        String profileImgUrl = s3Service.uploadProfileImage(profileImg, Role.HAIR_DESIGNER);

        User user = userRepository.findById(designerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        if (designerJpaRepository.existsById(designerId))
            throw new ConflictException(ErrorCode.ALREADY_EXIST_USER_EXCEPTION);
        user.update(request.name(), request.gender(), request.phoneNumber(), request.isMarketingAgree(), profileImgUrl, Role.HAIR_DESIGNER);
        HairShop hairShop = new HairShop(request.hairShop().name(), request.hairShop().address(),request.hairShop().detailAddress());
        Portfolio portfolio = new Portfolio(request.portfolio().instagramUrl(),request.portfolio().naverPlaceUrl());

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
    public void deleteDesignerInfo(final User designer) {
        dayOffJpaRepository.deleteAllByDesignerId(designer.getId());
        s3Service.deleteS3Image(designer.getProfileImgUrl());
        designerJpaRepository.deleteById(designer.getId());
    }
}
