package com.moddy.server.service.designer;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.ConflictException;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.designer.dto.HairShopDto;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.request.DesignerUpdateRequest;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.controller.user.dto.request.UserUpdateDto;
import com.moddy.server.domain.day_off.DayOfWeek;
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

import java.util.List;

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
    public UserCreateResponse createDesigner(final Long designerId, final DesignerCreateRequest request, final MultipartFile profileImg) {

        String profileImgUrl = s3Service.uploadProfileImage(profileImg, Role.HAIR_DESIGNER);

        User user = userRepository.findById(designerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        if (designerJpaRepository.existsById(designerId))
            throw new ConflictException(ErrorCode.ALREADY_EXIST_USER_EXCEPTION);
        user.update(request.name(), request.gender(), request.phoneNumber(), request.isMarketingAgree(), profileImgUrl, Role.HAIR_DESIGNER);
        HairShop hairShop = new HairShop(request.hairShop().name(), request.hairShop().address(), request.hairShop().detailAddress());
        Portfolio portfolio = new Portfolio(request.portfolio().instagramUrl(), request.portfolio().naverPlaceUrl());

        designerJpaRepository.designerRegister(user.getId(), hairShop.getAddress(), hairShop.getDetailAddress(), hairShop.getName(), portfolio.getInstagramUrl(), portfolio.getNaverPlaceUrl(), request.introduction(), request.kakaoOpenChatUrl());
        createDesignerDayoffs(designerId, request.dayOffs());
        return authService.createUserToken(designerId.toString());
    }

    @Transactional
    public void updateDesigner(final Long designerId, final MultipartFile profileImg, final DesignerUpdateRequest designerUpdateRequest) {
        Designer designer = designerJpaRepository.findById(designerId).orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND_EXCEPTION));

        String profileImgUrl = designer.getProfileImgUrl();
        if (profileImg != null) {
            s3Service.deleteS3Image(profileImgUrl);
            profileImgUrl = s3Service.uploadProfileImage(profileImg, Role.HAIR_DESIGNER);
        }

        updateUserInfos(designerId, new UserUpdateDto(designerUpdateRequest.name(), designerUpdateRequest.gender(), designer.getPhoneNumber(), designer.getIsMarketingAgree()), profileImgUrl);
        designer.update(new HairShop(designerUpdateRequest.hairShop().name(), designerUpdateRequest.hairShop().address(), designerUpdateRequest.hairShop().detailAddress()), new Portfolio(designerUpdateRequest.portfolio().instagramUrl(), designer.getPortfolio().getNaverPlaceUrl()), designerUpdateRequest.introduction(), designerUpdateRequest.kakaoOpenChatUrl());
        designerJpaRepository.save(designer);
        deleteDesignerDayoffs(designerId);
        createDesignerDayoffs(designerId, designerUpdateRequest.dayOffs());
    }

    public void deleteDesignerInfo(final User designer) {
        deleteDesignerDayoffs(designer.getId());
        s3Service.deleteS3Image(designer.getProfileImgUrl());
        designerJpaRepository.deleteById(designer.getId());
    }

    private void updateUserInfos(final Long userId, final UserUpdateDto userUpdateDto, final String profilImgUrl) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        user.update(userUpdateDto.name(), userUpdateDto.gender(), userUpdateDto.phoneNumber(), userUpdateDto.isMarketingAgree(), profilImgUrl, Role.HAIR_DESIGNER);
    }

    private void deleteDesignerDayoffs(final Long designerId) {
        dayOffJpaRepository.deleteAllByDesignerId(designerId);
    }

    private void createDesignerDayoffs(final Long designerId, final List<DayOfWeek> dayOffs) {
        Designer designer = designerJpaRepository.findById(designerId).orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND_EXCEPTION));
        dayOffs.forEach(dayOffId -> {
            DayOff dayOff = DayOff.builder()
                    .dayOfWeek(dayOffId)
                    .designer(designer)
                    .build();
            dayOffJpaRepository.save(dayOff);
        });
    }

}
