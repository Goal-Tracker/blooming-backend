package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTeamRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalTeamRepository goalTeamRepository;

    protected Long 유효한_사용자_아이디 = 1L;

    protected GoalTeam 유효한_골_팀;
    protected GoalTeam 유효한_골_팀2;

    @BeforeEach
    void setUp() {
        Goal 유효한_골 = Goal.builder()
                         .name("골 제목")
                         .memo("골 메모")
                         .startDate(LocalDate.now())
                         .endDate(LocalDate.now().plusDays(40))
                         .managerId(유효한_사용자_아이디)
                         .build();

        Goal 유효한_골2 = Goal.builder()
                         .name("골 제목2")
                         .memo("골 메모2")
                         .startDate(LocalDate.now())
                         .endDate(LocalDate.now().plusDays(90))
                         .managerId(유효한_사용자_아이디)
                         .build();

        goalRepository.saveAll(List.of(유효한_골, 유효한_골2));

        User 골에_참여한_사용자_1 = User.builder()
                                .oAuthId("test1")
                                .oAuthType(OAuthType.KAKAO)
                                .name("테스트1")
                                .email(new Email("test@test.com"))
                                .color(ThemeColor.BABY_BLUE)
                                .build();

        userRepository.save(골에_참여한_사용자_1);
        유효한_사용자_아이디 = 골에_참여한_사용자_1.getId();

        유효한_골_팀 = new GoalTeam(골에_참여한_사용자_1, 유효한_골);
        유효한_골_팀2 = new GoalTeam(골에_참여한_사용자_1, 유효한_골2);

        goalTeamRepository.saveAll(List.of(유효한_골_팀, 유효한_골_팀2));
    }
}
