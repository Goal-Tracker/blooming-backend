package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
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
public class GoalRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    protected final long 테스트를_위한_시스템_현재_시간_설정값 = 10L;
    protected User 골_관리자_사용자;
    protected User 골에_참여한_사용자;
    protected Goal 유효한_골;
    protected Goal 현재_진행중인_골;
    protected Goal 이미_종료된_골;
    protected Goal 이미_종료된_골2;
    protected List<Goal> 사용자가_참여한_골_목록 = new ArrayList<>();
    private List<User> 골에_참여한_사용자_목록 = new ArrayList<>();
    protected List<Goal> 사용자가_참여한_골_중_현재_진행중인_골_목록 = new ArrayList<>();
    protected List<Goal> 사용자가_참여한_골_중_종료된_골_목록 = new ArrayList<>();

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

        골에_참여한_사용자 = User.builder()
                         .oAuthId("아이디2")
                         .oAuthType(OAuthType.KAKAO)
                         .email(new Email("test2@gmail.com"))
                         .name(new Name("테스트2"))
                         .color(ThemeColor.BLUE)
                         .statusMessage("상태메시지2")
                         .build();

        userRepository.saveAll(List.of(골_관리자_사용자, 골에_참여한_사용자));
        골에_참여한_사용자_목록.addAll(List.of(골_관리자_사용자, 골에_참여한_사용자));

        유효한_골 = Goal.builder()
                    .name("골 제목")
                    .memo("골 메모")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 + 1L))
                    .managerId(골_관리자_사용자.getId())
                    .users(골에_참여한_사용자_목록)
                    .build();
        현재_진행중인_골 = Goal.builder()
                        .name("골 제목2")
                        .memo("골 메모2")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 + 1L))
                        .managerId(골_관리자_사용자.getId())
                        .users(골에_참여한_사용자_목록)
                        .build();
        이미_종료된_골 = Goal.builder()
                       .name("이미 종료된 골1")
                       .memo("이미 종료된 골2")
                       .startDate(LocalDate.now())
                       .endDate(LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 - 1L))
                       .managerId(골_관리자_사용자.getId())
                       .users(골에_참여한_사용자_목록)
                       .build();
        이미_종료된_골2 = Goal.builder()
                        .name("이미 종료된 골1")
                        .memo("이미 종료된 골2")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 - 1L))
                        .managerId(골_관리자_사용자.getId())
                        .users(골에_참여한_사용자_목록)
                        .build();

        goalRepository.saveAll(List.of(유효한_골, 현재_진행중인_골, 이미_종료된_골, 이미_종료된_골2));
        사용자가_참여한_골_목록.addAll(List.of(유효한_골, 현재_진행중인_골, 이미_종료된_골, 이미_종료된_골2));
        사용자가_참여한_골_중_현재_진행중인_골_목록.addAll(List.of(유효한_골, 현재_진행중인_골));
        사용자가_참여한_골_중_종료된_골_목록.addAll(List.of(이미_종료된_골, 이미_종료된_골2));
    }
}
