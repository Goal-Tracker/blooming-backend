package com.backend.blooming.report.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.GoalReport;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalReportRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalReportRepository goalReportRepository;

    protected Long 신고자_아이디;
    protected Long 골_아이디;
    protected Long 이미_신고한_골_아이디;

    @BeforeEach
    void setUpFixture() {
        final User 골_관리자 = User.builder()
                               .oAuthId("12345")
                               .oAuthType(OAuthType.KAKAO)
                               .name(new Name("사용자1"))
                               .email(new Email("user1@email.com"))
                               .build();
        final User 신고자 = User.builder()
                             .oAuthId("12346")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자2"))
                             .email(new Email("user2@email.com"))
                             .build();
        userRepository.saveAll(List.of(골_관리자, 신고자));
        신고자_아이디 = 신고자.getId();

        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(1))
                           .managerId(골_관리자.getId())
                           .users(List.of(골_관리자, 신고자))
                           .build();
        final Goal 이미_신고한_골 = Goal.builder()
                                  .name("골 제목2")
                                  .memo("골 메모2")
                                  .startDate(LocalDate.now())
                                  .endDate(LocalDate.now().plusDays(1))
                                  .managerId(골_관리자.getId())
                                  .users(List.of(골_관리자, 신고자))
                                  .build();
        goalRepository.saveAll(List.of(골, 이미_신고한_골));
        골_아이디 = 골.getId();
        이미_신고한_골_아이디 = 이미_신고한_골.getId();

        final GoalReport 골_신고 = new GoalReport(신고자, 이미_신고한_골, new Content("신고합니다."));
        goalReportRepository.save(골_신고);
    }
}
