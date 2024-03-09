package com.backend.blooming.report.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalReportTestFixture {

    protected User 골_관리자;
    protected User 팀원이_아닌_사용자;
    protected Goal 골;
    protected Content 신고_내용 = new Content("신고합니다");

    @BeforeEach
    void setUpFixture() {
        골_관리자 = User.builder()
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
        ReflectionTestUtils.setField(골_관리자, "id", 1L);
        ReflectionTestUtils.setField(팀원이_아닌_사용자, "id", 2L);

        골 = Goal.builder()
                .name("골 제목")
                .memo("골 메모")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5L))
                .managerId(골_관리자.getId())
                .users(List.of(골_관리자))
                .build();
        ReflectionTestUtils.setField(골, "id", 1L);
    }
}
