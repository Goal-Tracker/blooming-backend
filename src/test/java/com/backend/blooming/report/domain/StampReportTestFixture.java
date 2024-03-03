package com.backend.blooming.report.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.stamp.domain.Day;
import com.backend.blooming.stamp.domain.Message;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class StampReportTestFixture {

    protected User 신고자;
    protected User 팀원이_아닌_사용자;
    protected Stamp 스탬프;
    protected Stamp 신고자가_생성한_스탬프;
    protected Content 신고_내용 = new Content("신고합니다");

    @BeforeEach
    void setUpFixture() {
        신고자 = User.builder()
                  .oAuthId("12345")
                  .oAuthType(OAuthType.KAKAO)
                  .name(new Name("사용자1"))
                  .email(new Email("user1@email.com"))
                  .build();
        팀원이_아닌_사용자 = User.builder()
                         .oAuthId("12345")
                         .oAuthType(OAuthType.KAKAO)
                         .name(new Name("사용자1"))
                         .email(new Email("user1@email.com"))
                         .build();
        ReflectionTestUtils.setField(신고자, "id", 1L);
        ReflectionTestUtils.setField(팀원이_아닌_사용자, "id", 2L);

        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(5L))
                           .managerId(신고자.getId())
                           .users(List.of(신고자))
                           .build();
        ReflectionTestUtils.setField(골, "id", 1L);

        스탬프 = Stamp.builder()
                   .goal(골)
                   .user(신고자)
                   .day(new Day(골.getGoalTerm(), 1))
                   .message(new Message("테스트 메시지"))
                   .build();
        ReflectionTestUtils.setField(스탬프, "id", 1L);
        신고자가_생성한_스탬프 = 스탬프;
    }
}
