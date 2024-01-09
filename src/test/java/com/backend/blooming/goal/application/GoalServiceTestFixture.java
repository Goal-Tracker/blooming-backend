package com.backend.blooming.goal.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserNameDto;
import com.backend.blooming.goal.infrastructure.repository.dto.GoalTeamWithUserQueryProjectionDto;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@DirtiesContext
public class GoalServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalTeamRepository goalTeamRepository;

    protected Long 유효한_사용자_아이디;
    protected String 골_제목 = "골 제목";
    protected String 골_메모 = "골 메모";
    protected LocalDate 골_시작일 = LocalDate.now();
    protected LocalDate 골_종료일 = LocalDate.now().plusDays(40);
    protected long 골_날짜수 = 41L;
    protected long 현재_진행중인_날짜수 = 1L;
    protected List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>();
    protected CreateGoalDto 유효한_골_생성_dto;
    protected Goal 유효한_골;
    protected Goal 유효한_골2;
    protected Goal 유효한_골3;
    protected ReadGoalDetailDto 유효한_골_dto;
    protected CreateGoalDto 존재하지_않는_사용자가_관리자인_골_생성_dto;
    protected CreateGoalDto 존재하지_않는_사용자가_참여자로_있는_골_생성_dto;
    protected CreateGoalDto 골_시작날짜가_현재보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_종료날짜가_현재보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_종료날짜가_시작날짜보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_날짜수가_100_초과인_골_생성_dto;
    protected Long 존재하지_않는_골_아이디 = 997L;
    protected Long 유효한_골_아이디;
    protected GoalTeamWithUserNameDto 골에_참여한_사용자_1;
    protected GoalTeamWithUserNameDto 골에_참여한_사용자_2;
    protected List<GoalTeamWithUserNameDto> 골1에_참여한_사용자_정보를_포함한_골_팀_리스트 = new ArrayList<>();
    protected List<GoalTeamWithUserNameDto> 골2에_참여한_사용자_정보를_포함한_골_팀_리스트 = new ArrayList<>();
    protected List<GoalTeamWithUserNameDto> 골3에_참여한_사용자_정보를_포함한_골_팀_리스트 = new ArrayList<>();
    protected ReadAllGoalDto 사용자가_참여한_골_dto1;
    protected ReadAllGoalDto 사용자가_참여한_골_dto2;
    protected ReadAllGoalDto 사용자가_참여한_골_dto3;

    @BeforeEach
    void setUp() {
        User 유효한_사용자;
        User 유효한_사용자_2;
        List<GoalTeam> 골에_등록된_골_팀_목록 = new ArrayList<>();
        GoalTeam 유효한_골_팀;
        GoalTeam 유효한_골_팀2;
        GoalTeam 유효한_골_팀3;
        GoalTeam 유효한_골_팀4;
        Long 존재하지_않는_사용자_아이디 = 998L;
        List<Long> 존재하지_않는_사용자가_있는_사용자_아이디_목록 = new ArrayList<>();

        유효한_사용자 = User.builder()
                      .oAuthId("아이디")
                      .oAuthType(OAuthType.KAKAO)
                      .email(new Email("test@gmail.com"))
                      .name("테스트")
                      .color(ThemeColor.BABY_BLUE)
                      .statusMessage("상태메시지")
                      .build();

        유효한_사용자_2 = User.builder()
                        .oAuthId("아이디2")
                        .oAuthType(OAuthType.KAKAO)
                        .email(new Email("test2@gmail.com"))
                        .name("테스트2")
                        .color(ThemeColor.BABY_BLUE)
                        .statusMessage("상태메시지2")
                        .build();

        userRepository.saveAll(List.of(유효한_사용자, 유효한_사용자_2));
        유효한_사용자_아이디 = 유효한_사용자.getId();

        유효한_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                유효한_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        유효한_골 = Goal.builder()
                    .name(골_제목)
                    .memo(골_메모)
                    .startDate(골_시작일)
                    .endDate(골_종료일)
                    .managerId(유효한_사용자_아이디)
                    .build();

        유효한_골2 = Goal.builder()
                    .name("골 제목2")
                    .memo("골 메모2")
                    .startDate(골_시작일)
                    .endDate(LocalDate.now().plusDays(30))
                    .managerId(유효한_사용자_아이디)
                    .build();

        유효한_골3 = Goal.builder()
                    .name("골 제목3")
                    .memo("골 메모3")
                    .startDate(골_시작일)
                    .endDate(LocalDate.now().plusDays(60))
                    .managerId(유효한_사용자_아이디)
                    .build();

        goalRepository.saveAll(List.of(유효한_골, 유효한_골2, 유효한_골3));

        유효한_골_아이디 = 유효한_골.getId();
        유효한_골_팀 = new GoalTeam(유효한_사용자, 유효한_골);
        유효한_골_팀2 = new GoalTeam(유효한_사용자_2, 유효한_골);
        유효한_골_팀3 = new GoalTeam(유효한_사용자, 유효한_골2);
        유효한_골_팀4 = new GoalTeam(유효한_사용자, 유효한_골3);
        goalTeamRepository.saveAll(List.of(유효한_골_팀, 유효한_골_팀2, 유효한_골_팀3, 유효한_골_팀4));

        골에_등록된_골_팀_목록.add(유효한_골_팀);
        유효한_골.updateTeams(골에_등록된_골_팀_목록);
        골_팀에_등록된_사용자_아이디_목록.add(유효한_사용자_아이디);

        유효한_골_dto = new ReadGoalDetailDto(
                유효한_골.getId(),
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_날짜수,
                현재_진행중인_날짜수,
                유효한_사용자_아이디,
                골1에_참여한_사용자_정보를_포함한_골_팀_리스트
        );

        존재하지_않는_사용자가_관리자인_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                존재하지_않는_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        존재하지_않는_사용자가_있는_사용자_아이디_목록.add(존재하지_않는_사용자_아이디);

        존재하지_않는_사용자가_참여자로_있는_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                유효한_사용자_아이디,
                존재하지_않는_사용자가_있는_사용자_아이디_목록
        );

        골_시작날짜가_현재보다_이전인_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                LocalDate.now().minusDays(2),
                골_종료일,
                유효한_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        골_종료날짜가_현재보다_이전인_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                골_시작일,
                LocalDate.now().minusDays(2),
                유효한_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        골_종료날짜가_시작날짜보다_이전인_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(2),
                유효한_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        골_날짜수가_100_초과인_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                LocalDate.now(),
                LocalDate.now().plusDays(100),
                유효한_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        골에_참여한_사용자_1 = new GoalTeamWithUserNameDto(
                유효한_사용자_아이디,
                유효한_사용자.getName(),
                유효한_사용자.getColor()
        );

        골에_참여한_사용자_2 = new GoalTeamWithUserNameDto(
                유효한_사용자_2.getId(),
                유효한_사용자_2.getName(),
                유효한_사용자_2.getColor()
        );

        골1에_참여한_사용자_정보를_포함한_골_팀_리스트.addAll(List.of(골에_참여한_사용자_1, 골에_참여한_사용자_2));
        골2에_참여한_사용자_정보를_포함한_골_팀_리스트.add(골에_참여한_사용자_1);
        골3에_참여한_사용자_정보를_포함한_골_팀_리스트.add(골에_참여한_사용자_1);

        사용자가_참여한_골_dto1 = new ReadAllGoalDto(
                유효한_골.getId(),
                골_제목,
                LocalDate.now(),
                LocalDate.now().plusDays(100),
                유효한_골.getGoalTerm().getDays(),
                유효한_골.getGoalTerm().getInProgressDays(),
                골1에_참여한_사용자_정보를_포함한_골_팀_리스트
        );

        사용자가_참여한_골_dto2 = new ReadAllGoalDto(
                유효한_골2.getId(),
                유효한_골2.getName(),
                유효한_골2.getGoalTerm().getStartDate(),
                유효한_골2.getGoalTerm().getEndDate(),
                유효한_골2.getGoalTerm().getDays(),
                유효한_골2.getGoalTerm().getInProgressDays(),
                골2에_참여한_사용자_정보를_포함한_골_팀_리스트
        );

        사용자가_참여한_골_dto3 = new ReadAllGoalDto(
                유효한_골3.getId(),
                유효한_골3.getName(),
                유효한_골3.getGoalTerm().getStartDate(),
                유효한_골3.getGoalTerm().getEndDate(),
                유효한_골3.getGoalTerm().getDays(),
                유효한_골3.getGoalTerm().getInProgressDays(),
                골3에_참여한_사용자_정보를_포함한_골_팀_리스트
        );
    }
}
