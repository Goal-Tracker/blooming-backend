package com.backend.blooming.report.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.report.application.dto.CreateUserReportDto;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserReportServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    protected CreateUserReportDto 사용자_신고_요청_dto;
    protected CreateUserReportDto 존재하지_않는_사람의_사용자_신고_요청_dto;
    protected CreateUserReportDto 존재하지_않는_사람을_사용자_신고_요청_dto;

    @BeforeEach
    void setUpFixture() {
        final User 사용자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자"))
                             .email(new Email("test@email.com"))
                             .build();
        final User 사용자2 = User.builder()
                              .oAuthId("12346")
                              .oAuthType(OAuthType.KAKAO)
                              .name(new Name("사용자2"))
                              .email(new Email("test2@email.com"))
                              .build();
        userRepository.saveAll(List.of(사용자, 사용자2));

        final String 신고_내용 = "신고합니다";
        final Long 존재하지_않는_사용자_아이디 = 999L;
        사용자_신고_요청_dto = new CreateUserReportDto(사용자.getId(), 사용자2.getId(), 신고_내용);
        존재하지_않는_사람의_사용자_신고_요청_dto = new CreateUserReportDto(존재하지_않는_사용자_아이디, 사용자2.getId(), 신고_내용);
        존재하지_않는_사람을_사용자_신고_요청_dto = new CreateUserReportDto(사용자.getId(), 존재하지_않는_사용자_아이디, 신고_내용);
    }
}
