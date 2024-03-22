package com.backend.blooming.stamp.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.stamp.domain.Day;
import com.backend.blooming.stamp.domain.Message;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class StampRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private StampRepository stampRepository;

    protected User 골_관리자_사용자;
    protected Long 유효한_골_아이디;
    protected Stamp 유효한_스탬프;
    protected Stamp 유효한_스탬프2;
    protected long 유효한_스탬프_날짜;
    protected Goal 유효한_골;

    @BeforeEach
    void setUp() {
        골_관리자_사용자 = User.builder()
                        .oAuthId("아이디")
                        .oAuthType(OAuthType.KAKAO)
                        .email(new Email("test@gmail.com"))
                        .name(new Name("테스트"))
                        .color(ThemeColor.BABY_BLUE)
                        .statusMessage("상태메시지")
                        .build();
        User 골_참여_사용자1 = User.builder()
                             .oAuthId("아이디2")
                             .oAuthType(OAuthType.KAKAO)
                             .email(new Email("test2@gmail.com"))
                             .name(new Name("테스트2"))
                             .color(ThemeColor.INDIGO)
                             .statusMessage("상태메시지2")
                             .build();
        userRepository.saveAll(List.of(골_관리자_사용자, 골_참여_사용자1));
        List<User> 골에_참여한_사용자_목록 = new ArrayList<>(List.of(골_관리자_사용자));

        유효한_골 = Goal.builder()
                         .name("골 제목")
                         .memo("골 메모")
                         .startDate(LocalDate.now())
                         .endDate(LocalDate.now().plusDays(10))
                         .managerId(골_관리자_사용자.getId())
                         .users(골에_참여한_사용자_목록)
                         .build();
        goalRepository.saveAll(List.of(유효한_골));
        유효한_골_아이디 = 유효한_골.getId();

        유효한_스탬프 = Stamp.builder()
                             .goal(유효한_골)
                             .user(골_관리자_사용자)
                             .day(new Day(유효한_골.getGoalTerm(), 1))
                             .message(new Message("스탬프 메시지"))
                             .build();
        유효한_스탬프2 = Stamp.builder()
                             .goal(유효한_골)
                             .user(골_참여_사용자1)
                             .day(new Day(유효한_골.getGoalTerm(), 1))
                             .message(new Message("스탬프 메시지2"))
                             .build();
        stampRepository.saveAll(List.of(유효한_스탬프, 유효한_스탬프2));
        유효한_스탬프_날짜 = 유효한_스탬프.getDay();
    }
}
