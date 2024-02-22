package com.backend.blooming.report.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.report.application.dto.CreateUserReportDto;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.UserReport;
import com.backend.blooming.report.infrastructure.repository.UserReportRepository;
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

    @Autowired
    private UserReportRepository userReportRepository;

    protected CreateUserReportDto 사용자_신고_요청_dto;
    protected CreateUserReportDto 이미_신고한_사용자_신고_요청_dto;
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
        final User 이미_신고_받은_사용자 = User.builder()
                                      .oAuthId("12347")
                                      .oAuthType(OAuthType.KAKAO)
                                      .name(new Name("사용자3"))
                                      .email(new Email("test3@email.com"))
                                      .build();
        userRepository.saveAll(List.of(사용자, 사용자2, 이미_신고_받은_사용자));

        final String 신고_내용 = "신고합니다";
        final UserReport 사용자_신고 = new UserReport(사용자, 이미_신고_받은_사용자, new Content(신고_내용));
        userReportRepository.save(사용자_신고);

        final Long 존재하지_않는_사용자_아이디 = 999L;
        사용자_신고_요청_dto = new CreateUserReportDto(사용자.getId(), 사용자2.getId(), 신고_내용);
        이미_신고한_사용자_신고_요청_dto = new CreateUserReportDto(사용자.getId(), 이미_신고_받은_사용자.getId(), 신고_내용);
        존재하지_않는_사람의_사용자_신고_요청_dto = new CreateUserReportDto(존재하지_않는_사용자_아이디, 사용자2.getId(), 신고_내용);
        존재하지_않는_사람을_사용자_신고_요청_dto = new CreateUserReportDto(사용자.getId(), 존재하지_않는_사용자_아이디, 신고_내용);
    }
}
