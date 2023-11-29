package com.backend.blooming.goal.application;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.application.util.DateFormat;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalServiceFixture extends DateFormat {

    @Autowired
    private UserRepository userRepository;

    protected String 사용자_oauth_아이디 = "아이디";
    protected String 사용자_이메일 = "test@gmail.com";
    protected String 사용자_이름 = "테스트";
    protected Long 골_아이디 = 1L;
    protected String 골_제목 = "골 제목";
    protected String 골_메모 = "골 메모";
    protected String 골_시작일 = LocalDate.now().toString();
    protected String 골_종료일 = "2023-12-31";
    protected Date 골_시작일_date = dateFormatter(골_시작일);
    protected Date 골_종료일_date = dateFormatter(골_종료일);
    protected int 골_날짜수 = 40;
    protected User 유효한_사용자;
    protected Long 유효한_사용자_아이디;
    protected CreateGoalDto 유효한_골_생성_dto;
    protected GoalDto 유효한_골_dto;
    protected Goal 유효한_골;
    protected List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>();
    protected List<GoalTeam> 골에_등록된_골_팀_목록 = new ArrayList<>();
    protected GoalTeam 유효한_골_팀;

    @BeforeEach
    void setUp() {
        유효한_사용자 = User.builder()
                .oauthId(사용자_oauth_아이디)
                .email(사용자_이메일)
                .name(사용자_이름)
                .build();

        userRepository.save(유효한_사용자);
        유효한_사용자_아이디 = 유효한_사용자.getId();
        골_팀에_등록된_사용자_아이디_목록.add(유효한_사용자_아이디);

        유효한_골_생성_dto = new CreateGoalDto(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_날짜수,
                골_팀에_등록된_사용자_아이디_목록
        );

        유효한_골_dto = new GoalDto(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일_date,
                골_종료일_date,
                골_날짜수,
                골_팀에_등록된_사용자_아이디_목록
        );

        유효한_골 = Goal.builder()
                .goalName(골_제목)
                .goalMemo(골_메모)
                .goalStartDay(골_시작일_date)
                .goalEndDay(골_종료일_date)
                .goalDays(골_날짜수)
                .build();

        유효한_골_팀 = GoalTeam.builder().user(유효한_사용자).goal(유효한_골).build();
        골에_등록된_골_팀_목록.add(유효한_골_팀);
        유효한_골.updateGoalTeams(골에_등록된_골_팀_목록);
        골_팀에_등록된_사용자_아이디_목록 = 유효한_골.getGoalTeamIds();
    }
}
