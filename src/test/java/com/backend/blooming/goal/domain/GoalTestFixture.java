package com.backend.blooming.goal.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTestFixture {

    protected String 골_제목 = "골 제목";
    protected LocalDate 골_시작일 = LocalDate.now();
    protected LocalDate 골_종료일 = LocalDate.now().plusDays(40);
    protected Long 골_관리자_아이디 = 1L;
    protected List<User> 골_참여자_목록 = new ArrayList<>();
    protected List<User> 유효하지_않은_골_참여자_목록 = new ArrayList<>();
    private User 유효한_사용자 = User.builder()
                               .oAuthId("아이디")
                               .oAuthType(OAuthType.KAKAO)
                               .email(new Email("test@gmail.com"))
                               .name(new Name("테스트"))
                               .color(ThemeColor.BABY_BLUE)
                               .statusMessage("상태메시지")
                               .build();
    private User 유효한_사용자_2 = User.builder()
                                 .oAuthId("아이디2")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test2@gmail.com"))
                                 .name(new Name("테스트2"))
                                 .color(ThemeColor.BABY_BLUE)
                                 .statusMessage("상태메시지2")
                                 .build();

    @BeforeEach
    void setUp() {
        골_참여자_목록.addAll(List.of(유효한_사용자, 유효한_사용자_2));
        유효하지_않은_골_참여자_목록.addAll(List.of(유효한_사용자, 유효한_사용자, 유효한_사용자, 유효한_사용자, 유효한_사용자, 유효한_사용자));
    }
}
