package com.backend.blooming.report.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.report.domain.Content;
import com.backend.blooming.report.domain.StampReport;
import com.backend.blooming.stamp.domain.Day;
import com.backend.blooming.stamp.domain.Message;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.stamp.infrastructure.repository.StampRepository;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class StampReportRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private StampRepository stampRepository;

    @Autowired
    private StampReportRepository stampReportRepository;

    protected Long 신고자_아이디;
    protected Long 이미_신고한_사용자_아이디;
    protected Long 스탬프_아이디;

    @BeforeEach
    void setUpFixture() {
        final User 신고자 = User.builder()
                             .oAuthId("12345")
                             .oAuthType(OAuthType.KAKAO)
                             .name(new Name("사용자1"))
                             .email(new Email("user1@email.com"))
                             .build();
        final User 스탬프_작성자 = User.builder()
                                 .oAuthId("12346")
                                 .oAuthType(OAuthType.KAKAO)
                                 .name(new Name("사용자2"))
                                 .email(new Email("user2@email.com"))
                                 .build();
        final User 이미_신고한_사용자 = User.builder()
                                    .oAuthId("12347")
                                    .oAuthType(OAuthType.KAKAO)
                                    .name(new Name("사용자3"))
                                    .email(new Email("user3@email.com"))
                                    .build();
        userRepository.saveAll(List.of(신고자, 스탬프_작성자, 이미_신고한_사용자));
        신고자_아이디 = 신고자.getId();
        이미_신고한_사용자_아이디 = 이미_신고한_사용자.getId();

        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(5))
                           .managerId(신고자_아이디)
                           .users(List.of(신고자, 스탬프_작성자, 이미_신고한_사용자))
                           .build();
        goalRepository.save(골);

        final Stamp 스탬프 = Stamp.builder()
                               .goal(골)
                               .user(스탬프_작성자)
                               .day(new Day(골.getGoalTerm(), 1))
                               .message(new Message("스탬프 메시지"))
                               .build();
        stampRepository.save(스탬프);
        스탬프_아이디 = 스탬프.getId();

        final StampReport 스탬프_신고 = new StampReport(이미_신고한_사용자, 스탬프, new Content("신고합니다."));
        stampReportRepository.save(스탬프_신고);
    }
}
