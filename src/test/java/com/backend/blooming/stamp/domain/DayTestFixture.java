package com.backend.blooming.stamp.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class DayTestFixture {

    private User 유효한_사용자 = User.builder()
                                 .oAuthId("아이디")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test@gmail.com"))
                                 .name(new Name("테스트"))
                                 .color(ThemeColor.BABY_BLUE)
                                 .statusMessage("상태메시지")
                                 .build();
    private User 골_참여_사용자 = User.builder()
                                .oAuthId("아이디2")
                                .oAuthType(OAuthType.KAKAO)
                                .email(new Email("test2@gmail.com"))
                                .name(new Name("테스트2"))
                                .color(ThemeColor.BABY_BLUE)
                                .statusMessage("상태메시지2")
                                .build();
    private List<User> 골_참여_사용자_목록 = new ArrayList<>(List.of(유효한_사용자, 골_참여_사용자));
    protected Goal 유효한_골 = Goal.builder()
                               .name("골 제목")
                               .memo("골 메모")
                               .startDate(LocalDate.now())
                               .endDate(LocalDate.now().plusDays(20))
                               .managerId(1L)
                               .users(골_참여_사용자_목록)
                               .build();
    protected Goal 스탬프_생성날짜가_골_시작일_보다_이전인_골 = Goal.builder()
                                                  .name("골 제목")
                                                  .memo("골 메모")
                                                  .startDate(LocalDate.now().plusDays(1))
                                                  .endDate(LocalDate.now().plusDays(20))
                                                  .managerId(1L)
                                                  .users(골_참여_사용자_목록)
                                                  .build();
    protected static int 스탬프_날짜가_골_시작일_이전인_경우 = 0;
    protected static int 스탬프_날짜가_현재_기준_스탬프_날짜보다_큰_경우 = 2;
}
