package com.backend.blooming.goal.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.infrastructure.repository.GoalTeamRepository;
import com.backend.blooming.themecolor.domain.ThemeColor;
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
    protected long 골_날짜수 = 40L;
    protected List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>();
    protected CreateGoalDto 유효한_골_생성_dto;
    protected Goal 유효한_골;
    protected GoalDto 유효한_골_dto;
    protected CreateGoalDto 존재하지_않는_사용자가_관리자인_골_생성_dto;
    protected CreateGoalDto 존재하지_않는_사용자가_참여자로_있는_골_생성_dto;
    protected CreateGoalDto 골_시작날짜가_현재보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_종료날짜가_현재보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_종료날짜가_시작날짜보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_날짜수가_1_미만인_골_생성_dto;
    protected Long 존재하지_않는_골_아이디 = 997L;

    @BeforeEach
    void setUp() {
        User 유효한_사용자;
        List<GoalTeam> 골에_등록된_골_팀_목록 = new ArrayList<>();
        GoalTeam 유효한_골_팀;
        Long 존재하지_않는_사용자_아이디 = 998L;
        List<Long> 존재하지_않는_사용자가_있는_사용자_아이디_목록 = new ArrayList<>();

        유효한_사용자 = User.builder()
                      .oAuthId("아이디")
                      .oAuthType(OAuthType.KAKAO)
                      .email("test@gmail.com")
                      .name("테스트")
                      .color(ThemeColor.BABY_BLUE)
                      .statusMessage("상태메시지")
                      .build();

        유효한_사용자 = userRepository.save(유효한_사용자);
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

        goalRepository.save(유효한_골);

        유효한_골_팀 = new GoalTeam(유효한_사용자, 유효한_골);
        goalTeamRepository.save(유효한_골_팀);

        골에_등록된_골_팀_목록.add(유효한_골_팀);
        유효한_골.updateGoalTeams(골에_등록된_골_팀_목록);
        골_팀에_등록된_사용자_아이디_목록.add(유효한_사용자_아이디);

        유효한_골_dto = new GoalDto(
                유효한_골.getId(),
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_날짜수,
                유효한_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
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

        골_날짜수가_1_미만인_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                LocalDate.now(),
                LocalDate.now(),
                유효한_사용자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );
    }
}
