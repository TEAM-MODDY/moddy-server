package com.moddy.server.service.auth;

import com.moddy.server.common.dto.TokenPair;
import com.moddy.server.common.exception.model.BadRequestException;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.common.exception.model.NotFoundUserException;
import com.moddy.server.common.util.SmsUtil;
import com.moddy.server.common.util.VerificationCodeGenerator;
import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.controller.auth.dto.response.LoginResponseDto;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.external.kakao.service.KakaoSocialService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.moddy.server.common.exception.enums.ErrorCode.INVALID_PHONE_NUMBER_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.NOT_FOUND_VERIFICATION_CODE_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.NOT_MATCH_VERIFICATION_CODE_EXCEPTION;
import static com.moddy.server.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final SmsUtil smsUtil;
    private final JwtService jwtService;
    private final KakaoSocialService kakaoSocialService;
    private final UserRepository userRepository;

    public LoginResponseDto login(final String baseUrl, final String kakaoCode) {
        String kakaoId = kakaoSocialService.getIdFromKakao(baseUrl, kakaoCode);
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        if (user.isEmpty()) {
            User newUser = User.builder()
                    .kakaoId(kakaoId)
                    .build();
            userRepository.save(newUser);

            TokenPair tokenPair = jwtService.generateTokenPair(String.valueOf(newUser.getId()));
            throw new NotFoundUserException(USER_NOT_FOUND_EXCEPTION, tokenPair);
        } else if (user.get().getRole() == null) {
            TokenPair tokenPair = jwtService.generateTokenPair(String.valueOf(user.get().getId()));
            throw new NotFoundUserException(USER_NOT_FOUND_EXCEPTION, tokenPair);
        }
        TokenPair tokenPair = jwtService.generateTokenPair(String.valueOf(user.get().getId()));
        return new LoginResponseDto(tokenPair.accessToken(), tokenPair.refreshToken(), user.get().getRole().name());
    }

    public UserCreateResponse createUserToken(String useId) {

        TokenPair tokenPair = jwtService.generateTokenPair(useId);
        UserCreateResponse userCreateResponse = new UserCreateResponse(tokenPair.accessToken(), tokenPair.refreshToken());

        return userCreateResponse;
    }

    @Transactional
    public void sendVerificationCodeMessageToUser(final String phoneNumber) throws IOException {
        final String verificationCode = VerificationCodeGenerator.generate();

        if (!smsUtil.sendVerificationCode(phoneNumber, verificationCode))
            throw new BadRequestException(INVALID_PHONE_NUMBER_EXCEPTION);

        smsUtil.saveVerificationCode(phoneNumber, verificationCode);
    }

    @Transactional
    public void verifyCode(final String phoneNumber, final String verificationCode) {
        if (!smsUtil.isVerificationCode(phoneNumber))
            throw new NotFoundException(NOT_FOUND_VERIFICATION_CODE_EXCEPTION);

        if (!smsUtil.getVerificationCode(phoneNumber).equals(verificationCode))
            throw new BadRequestException(NOT_MATCH_VERIFICATION_CODE_EXCEPTION);

        smsUtil.deleteVerificationCode(phoneNumber);
    }

    public void logout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
    }
}
