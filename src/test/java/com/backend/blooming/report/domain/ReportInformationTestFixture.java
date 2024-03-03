package com.backend.blooming.report.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
public class ReportInformationTestFixture {

    protected User 신고자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자1"))
                             .email(new Email("user1@email.com"))
                             .build();
    protected Content 신고_내용 = new Content("신고합니다");

    @BeforeEach
    void setUpFixture() {
        ReflectionTestUtils.setField(신고자, "id", 1L);
    }
}
