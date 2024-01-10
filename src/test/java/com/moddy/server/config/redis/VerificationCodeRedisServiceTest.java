package com.moddy.server.config.redis;

//@SpringBootTest
class VerificationCodeRedisServiceTest {
//    @Autowired
//    private VerificationCodeRedisService verificationCodeRedisService;
//
//    @Test
//    @DisplayName("전화 번호를 키로 사용하여 인증 코드를 저장할 수 있다.")
//    public void saveVerificationCodeTest() {
//        // given
//        String phoneNumber = "01000000000";
//        String verificationCode = "123456";
//
//        // when
//        verificationCodeRedisService.saveVerificationCode(phoneNumber, verificationCode);
//
//        // then
//        String response = verificationCodeRedisService.getVerificationCode(phoneNumber);
//        assertThat(response).isEqualTo(verificationCode);
//    }
//
//    @Test
//    @DisplayName("특정 전화 번호의 값을 제거할 수 있다.")
//    public void deleteVerificationCodeTest() {
//        // given
//        String phoneNumber = "01000000000";
//        String verificationCode = "123456";
//        verificationCodeRedisService.saveVerificationCode(phoneNumber, verificationCode);
//
//        // when
//        verificationCodeRedisService.deleteVerificationCode(phoneNumber);
//
//        // then
//        boolean response = verificationCodeRedisService.isVerificationCodeEmpty(phoneNumber);
//        assertThat(response).isTrue();
//    }
}