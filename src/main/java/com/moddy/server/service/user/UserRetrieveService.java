package com.moddy.server.service.user;

import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.user.dto.response.UserDetailResponseDto;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.moddy.server.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserRetrieveService {
    private final UserRepository userRepository;

    public UserDetailResponseDto getUserDetail(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        return new UserDetailResponseDto(user.getId(), user.getProfileImgUrl(), user.getName(), user.getRole());
    }
}
