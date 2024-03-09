package com.backend.blooming.report.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.UserReport;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserReportRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserReportRepository userReportRepository;

    protected Long 신고자_아이디;
    protected Long 이미_신고한_신고_대상자_아이디;
    protected Long 신고하지_않은_사람_아이디;

    @BeforeEach
    void setUpFixture() {
        final User 신고자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자1"))
                             .email(new Email("user1@email.com"))
                             .build();
        final User 이미_신고한_신고_대상자 = User.builder()
                                       .oAuthId("12346")
                                       .oAuthType(OAuthType.KAKAO)
                                       .name(new Name("사용자2"))
                                       .email(new Email("user2@email.com"))
                                       .build();
        final User 신고하지_않은_사람 = User.builder()
                                    .oAuthId("12347")
                                    .oAuthType(OAuthType.KAKAO)
                                    .name(new Name("사용자3"))
                                    .email(new Email("user3@email.com"))
                                    .build();
        userRepository.saveAll(List.of(신고자, 이미_신고한_신고_대상자, 신고하지_않은_사람));
        신고자_아이디 = 신고자.getId();
        이미_신고한_신고_대상자_아이디 = 이미_신고한_신고_대상자.getId();
        신고하지_않은_사람_아이디 = 신고하지_않은_사람.getId();

        final UserReport report = new UserReport(신고자, 이미_신고한_신고_대상자, new Content("신고합니다"));
        userReportRepository.save(report);
    }
}
