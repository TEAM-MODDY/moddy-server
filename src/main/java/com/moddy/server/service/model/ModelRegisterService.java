package com.moddy.server.service.model;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.ConflictException;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.controller.model.dto.request.ModelCreateRequest;
import com.moddy.server.controller.model.dto.request.ModelUpdateRequest;
import com.moddy.server.controller.user.dto.request.UserUpdateDto;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_region.PreferRegion;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import com.moddy.server.domain.region.Region;
import com.moddy.server.domain.region.repository.RegionJpaRepository;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.external.s3.S3Service;
import com.moddy.server.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.moddy.server.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ModelRegisterService {
    private final PreferRegionJpaRepository preferRegionJpaRepository;
    private final ModelJpaRepository modelJpaRepository;
    private final RegionJpaRepository regionJpaRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final AuthService authService;

    @Transactional
    public UserCreateResponse createModel(final Long userId, ModelCreateRequest request) {
        if (modelJpaRepository.existsById(userId)) throw new ConflictException(ErrorCode.ALREADY_EXIST_MODEL_EXCEPTION);
        updateUserInfos(userId, request.userInfoUpdate());
        modelJpaRepository.modelRegister(userId, request.year());
        createModelPreferRegions(userId, request.preferRegions());

        return authService.createUserToken(userId.toString());
    }

    @Transactional
    public void updateModel(final Long modelId, ModelUpdateRequest modelUpdateRequest) {
        Model model = modelJpaRepository.findById(modelId).orElseThrow(() -> new NotFoundException(ErrorCode.MODEL_NOT_FOUND_EXCEPTION));

        updateUserInfos(modelId, new UserUpdateDto(modelUpdateRequest.name(), modelUpdateRequest.gender(), model.getPhoneNumber(), model.getIsMarketingAgree()));
        model.update(modelUpdateRequest.year());
        modelJpaRepository.save(model);
        deleteModelPreferRegions(modelId);
        createModelPreferRegions(modelId, modelUpdateRequest.preferRegions());
    }

    private void updateUserInfos(final Long userId, final UserUpdateDto userUpdateDto) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        user.update(userUpdateDto.name(), userUpdateDto.gender(), userUpdateDto.phoneNumber(), userUpdateDto.isMarketingAgree(), s3Service.getDefaultProfileImageUrl(), Role.MODEL);
    }

    private void createModelPreferRegions(final Long modelId, final List<Long> preferRegions) {
        Model model = modelJpaRepository.findById(modelId).orElseThrow(() -> new NotFoundException(ErrorCode.MODEL_NOT_FOUND_EXCEPTION));

        preferRegions.forEach(preferRegionId -> {
            Region region = regionJpaRepository.findById(preferRegionId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_REGION_EXCEPTION));
            PreferRegion preferRegion = new PreferRegion(model, region);
            preferRegionJpaRepository.save(preferRegion);
        });
    }

    public void deleteModelInfo(final Long modelId) {
        deleteModelPreferRegions(modelId);
        modelJpaRepository.deleteById(modelId);
    }

    private void deleteModelPreferRegions(final Long modelId) {
        preferRegionJpaRepository.deleteAllByModelId(modelId);
    }
}
