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
    protected List<User> 수정_요청한_골_참여자_목록 = new ArrayList<>();
    protected List<User> 기존_참여자가_포함되지_않은_수정_골_참여자_목록 = new ArrayList<>();
    protected User 기존_골_참여자 = User.builder()
                               .oAuthId("아이디")
                               .oAuthType(OAuthType.KAKAO)
                               .email(new Email("test@gmail.com"))
                               .name(new Name("테스트"))
                               .color(ThemeColor.BABY_BLUE)
                               .statusMessage("상태메시지")
                               .build();
    protected User 기존_골_참여자2 = User.builder()
                                 .oAuthId("아이디2")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test2@gmail.com"))
                                 .name(new Name("테스트2"))
                                 .color(ThemeColor.BABY_BLUE)
                                 .statusMessage("상태메시지2")
                                 .build();
    protected User 추가된_골_참여_사용자 = User.builder()
                                      .oAuthId("아이디3")
                                      .oAuthType(OAuthType.KAKAO)
                                      .email(new Email("test3@gmail.com"))
                                      .name(new Name("테스트3"))
                                      .color(ThemeColor.CORAL)
                                      .statusMessage("상태메시지3")
                                      .build();
    protected Goal 유효한_골 = Goal.builder()
                             .name(골_제목)
                             .memo("골 메모")
                             .startDate(골_시작일)
                             .endDate(골_종료일)
                             .managerId(골_관리자_아이디)
                             .users(골_참여자_목록)
                             .build();

    @BeforeEach
    void setUp() {
        골_참여자_목록.addAll(List.of(기존_골_참여자, 기존_골_참여자2));
        유효하지_않은_골_참여자_목록.addAll(List.of(기존_골_참여자, 기존_골_참여자, 기존_골_참여자, 기존_골_참여자, 기존_골_참여자, 기존_골_참여자));
        수정_요청한_골_참여자_목록.addAll(List.of(기존_골_참여자, 기존_골_참여자2, 추가된_골_참여_사용자));
        기존_참여자가_포함되지_않은_수정_골_참여자_목록.addAll(List.of(기존_골_참여자, 추가된_골_참여_사용자));
    }
}
