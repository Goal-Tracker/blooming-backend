package com.backend.blooming.goal.presentation;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.application.util.DateFormat;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.presentation.dto.request.GoalRequest;
import com.backend.blooming.goal.presentation.dto.response.GoalResponse;
import com.backend.blooming.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalControllerFixture extends DateFormat {

    protected Long 골_아이디 = 1L;
    protected String 골_제목 = "골 제목";
    protected String 골_메모 = "골 메모";
    protected String 골_시작일 = LocalDate.now().toString();
    protected String 골_종료일 = "2023-12-31";
    protected Date 골_시작일_date = dateFormatter(골_시작일);
    protected Date 골_종료일_date = dateFormatter(골_종료일);
    protected int 골_날짜수 = 40;
    protected List<GoalTeam> 골_팀_목록 = new ArrayList<>();
    protected List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>(List.of(1L));
    protected CreateGoalDto 유효한_골_생성_dto;
    protected String 사용자_아이디 = "아이디";
    protected String 사용자_이메일 = "test@gmail.com";
    protected String 사용자_이름 = "test";
    protected Goal 유효한_골;
    protected GoalDto 유효한_골_dto;
    protected GoalRequest 요청한_골_dto;
    protected GoalResponse 응답한_골_dto;

    @BeforeEach
    void setUp() {

        유효한_골_생성_dto = new CreateGoalDto(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_날짜수,
                골_팀에_등록된_사용자_아이디_목록
        );

        final User user = User.builder()
                .oauthId(사용자_아이디)
                .email(사용자_이메일)
                .name(사용자_이름)
                .build();

        final GoalTeam goalTeam1 = GoalTeam.builder().user(user).build();

        골_팀_목록.add(goalTeam1);

        유효한_골 = Goal.builder()
                .goalName(골_제목)
                .goalMemo(골_메모)
                .goalStartDay(골_시작일_date)
                .goalEndDay(골_종료일_date)
                .goalDays(골_날짜수)
                .build();

        요청한_골_dto = new GoalRequest(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_날짜수,
                골_팀에_등록된_사용자_아이디_목록
        );

        응답한_골_dto = new GoalResponse(
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
    }
}
