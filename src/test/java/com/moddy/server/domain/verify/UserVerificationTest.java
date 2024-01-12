package com.moddy.server.domain.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserVerificationTest {

    @Test
    @DisplayName("테이블 생성 시간이 2000년 2월 3일 6시 30분이고, 이후에 3분이 지났으면 true를 반환한다.")
    void isExpireCodeTest() {
        // given
        UserVerification userVerification = UserVerification
                .builder()
                .createdAt(LocalDateTime.of(2000, 2, 3, 6, 30))
                .build();
        LocalDateTime now = LocalDateTime.of(2000, 2, 3, 6, 33);

        // when
        boolean isExpire = userVerification.isExpireCode(now);

        // then
        assertThat(isExpire).isTrue();
    }

    @Test
    @DisplayName("테이블 생성 시간이 2000년 2월 3일 6시 30분이고, 이후에 2분이 지났으면 false를 반환한다.")
    void isExpireCodeTest2() {
        // given
        UserVerification userVerification = UserVerification
                .builder()
                .createdAt(LocalDateTime.of(2000, 2, 3, 6, 30))
                .build();
        LocalDateTime now = LocalDateTime.of(2000, 2, 3, 6, 32);

        // when
        boolean isExpire = userVerification.isExpireCode(now);

        // then
        assertThat(isExpire).isFalse();
    }
}