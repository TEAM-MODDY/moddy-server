package com.moddy.server.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class VerificationCodeGeneratorTest {

    @Test
    @DisplayName("인증 코드는 항상 6자리이다.")
    void generateTest() {
        // given

        // when
        String result = VerificationCodeGenerator.generate();
        // then
        assertThat(result.length()).isEqualTo(6);
    }
}
