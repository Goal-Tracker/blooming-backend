package com.backend.blooming.goal.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTeamTestFixture {

    protected User 유효한_사용자 = User.builder()
                                 .oAuthId("아이디")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test@gmail.com"))
                                 .name(new Name("테스트"))
                                 .color(ThemeColor.BABY_BLUE)
                                 .statusMessage("상태메시지")
                                 .build();
    protected User 유효한_골_참여_사용자 = User.builder()
                                      .oAuthId("아이디2")
                                      .oAuthType(OAuthType.KAKAO)
                                      .email(new Email("test2@gmail.com"))
                                      .name(new Name("테스트2"))
                                      .color(ThemeColor.INDIGO)
                                      .statusMessage("상태메시지2")
                                      .build();
    protected Goal 유효한_골;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(유효한_사용자, "id", 1L);
        유효한_골 = Goal.builder()
                    .name("골 제목")
                    .memo("골 메모")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(10))
                    .managerId(유효한_사용자.getId())
                    .users(List.of(유효한_사용자, 유효한_골_참여_사용자))
                    .build();
    }
}
