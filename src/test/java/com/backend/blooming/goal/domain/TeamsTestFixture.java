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
public class TeamsTestFixture {

    protected List<User> 골_참여_사용자_목록 = new ArrayList<>();
    protected List<User> 크기가_5보다_큰_골_참여_사용자_목록 = new ArrayList<>();
    protected List<User> 수정할_골_참여_사용자_목록 = new ArrayList<>();
    protected Goal 유효한_골;

    private User 골_참여자 = User.builder()
                             .oAuthId("아이디")
                             .oAuthType(OAuthType.KAKAO)
                             .email(new Email("test@gmail.com"))
                             .name(new Name("테스트"))
                             .color(ThemeColor.BABY_BLUE)
                             .statusMessage("상태메시지")
                             .build();
    private User 골_참여자2 = User.builder()
                              .oAuthId("아이디2")
                              .oAuthType(OAuthType.KAKAO)
                              .email(new Email("test2@gmail.com"))
                              .name(new Name("테스트2"))
                              .color(ThemeColor.BABY_BLUE)
                              .statusMessage("상태메시지2")
                              .build();
    private User 추가된_골_참여자 = User.builder()
                              .oAuthId("아이디3")
                              .oAuthType(OAuthType.KAKAO)
                              .email(new Email("test3@gmail.com"))
                              .name(new Name("테스트3"))
                              .color(ThemeColor.INDIGO)
                              .statusMessage("상태메시지3")
                              .build();

    @BeforeEach
    void setUp() {
        골_참여_사용자_목록.addAll(List.of(골_참여자, 골_참여자2));
        크기가_5보다_큰_골_참여_사용자_목록.addAll(List.of(골_참여자, 골_참여자2, 골_참여자, 골_참여자2, 골_참여자, 골_참여자2));
        유효한_골 = Goal.builder()
                    .name("골 제목")
                    .memo("골 메모")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(20))
                    .managerId(1L)
                    .users(골_참여_사용자_목록)
                    .build();
        수정할_골_참여_사용자_목록.addAll(List.of(골_참여자, 골_참여자2, 추가된_골_참여자));
    }
}
