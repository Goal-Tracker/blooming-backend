package com.backend.blooming.goal.infrastructure.repository;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserQueryProjectionDto;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalTeamWithUserNameRepositoryTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalTeamRepository goalTeamRepository;

    protected List<GoalTeamWithUserNameDto> 골에_참여한_사용자_정보를_포함한_골_팀_리스트 = new ArrayList<>();
    protected Long 참여한_골_아이디 = 1L;
    protected Long 참여한_사용자1_아이디 = 1L;
    protected String 참여한_사용자1_이름 = "테스트1";
    protected ThemeColor 참여한_사용자1_테마색상 = ThemeColor.BABY_BLUE;
    protected Long 참여한_사용자2_아이디 = 2L;
    protected String 참여한_사용자2_이름 = "테스트2";
    protected ThemeColor 참여한_사용자2_테마색상 = ThemeColor.BEIGE;

    protected User 골에_참여한_사용자_1 = User.builder()
                                      .oAuthId("test1")
                                      .oAuthType(OAuthType.KAKAO)
                                      .name(참여한_사용자1_이름)
                                      .email(new Email("test@test.com"))
                                      .color(참여한_사용자1_테마색상)
                                      .build();

    protected User 골에_참여한_사용자_2 = User.builder()
                                      .oAuthId("test2")
                                      .oAuthType(OAuthType.KAKAO)
                                      .name(참여한_사용자2_이름)
                                      .email(new Email("test@test.com"))
                                      .color(참여한_사용자2_테마색상)
                                      .build();

    protected Goal 유효한_골 = Goal.builder()
                               .name("골 제목")
                               .memo("골 메모")
                               .startDate(LocalDate.now())
                               .endDate(LocalDate.now().plusDays(40))
                               .managerId(참여한_사용자1_아이디)
                               .build();

    protected GoalTeamWithUserNameDto 골에_참여한_사용자_dto_1;
    protected GoalTeamWithUserNameDto 골에_참여한_사용자_dto_2;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(List.of(골에_참여한_사용자_1, 골에_참여한_사용자_2));
        goalRepository.save(유효한_골);

        GoalTeam 유효한_골_팀 = new GoalTeam(골에_참여한_사용자_1, 유효한_골);
        GoalTeam 유효한_골_팀2 = new GoalTeam(골에_참여한_사용자_2, 유효한_골);

        goalTeamRepository.saveAll(List.of(유효한_골_팀, 유효한_골_팀2));

        참여한_골_아이디 = 유효한_골.getId();
        참여한_사용자1_아이디 = 골에_참여한_사용자_1.getId();
        참여한_사용자2_아이디 = 골에_참여한_사용자_2.getId();

        골에_참여한_사용자_dto_1 = new GoalTeamWithUserNameDto(
                참여한_사용자1_아이디,
                참여한_사용자1_이름,
                참여한_사용자1_테마색상
        );

        골에_참여한_사용자_dto_2 = new GoalTeamWithUserNameDto(
                참여한_사용자2_아이디,
                참여한_사용자2_이름,
                참여한_사용자2_테마색상
        );

        골에_참여한_사용자_정보를_포함한_골_팀_리스트.add(골에_참여한_사용자_dto_1);
        골에_참여한_사용자_정보를_포함한_골_팀_리스트.add(골에_참여한_사용자_dto_2);
    }
}
