package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalControllerTestFixture {

    private String 골_제목 = "골 제목";
    private String 골_메모 = "골 메모";
    private LocalDate 골_시작일 = LocalDate.now();
    private LocalDate 골_종료일 = LocalDate.now().plusDays(40);
    private long 골_날짜수 = 41L;
    private long 현재_진행중인_날짜수 = 1L;
    private Long 골_관리자_아이디 = 1L;
    private List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>(List.of(1L, 2L, 3L));
    private List<Long> 존재하지_않는_사용자가_있는_사용자_아이디_목록 = new ArrayList<>(List.of(999L));

    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(골_관리자_아이디);
    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 유효한_골_아이디 = 1L;

    protected CreateGoalDto 유효한_골_생성_dto = new CreateGoalDto(
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected CreateGoalDto 존재하지_않는_사용자가_참여자로_있는_골_생성_dto = new CreateGoalDto(
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_관리자_아이디,
            존재하지_않는_사용자가_있는_사용자_아이디_목록
    );

    protected CreateGoalDto 골_시작날짜가_현재보다_이전인_골_생성_dto = new CreateGoalDto(
            골_제목,
            골_메모,
            LocalDate.now().minusDays(2),
            골_종료일,
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected CreateGoalDto 골_종료날짜가_현재보다_이전인_골_생성_dto = new CreateGoalDto(
            골_제목,
            골_메모,
            골_시작일,
            LocalDate.now().minusDays(2),
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected CreateGoalDto 골_종료날짜가_시작날짜보다_이전인_골_생성_dto = new CreateGoalDto(
            골_제목,
            골_메모,
            LocalDate.now().plusDays(4),
            LocalDate.now().plusDays(2),
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected CreateGoalDto 골_날짜수가_100_초과인_골_생성_dto = new CreateGoalDto(
            골_제목,
            골_메모,
            LocalDate.now(),
            LocalDate.now().plusDays(100),
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected CreateGoalRequest 요청한_골_dto = new CreateGoalRequest(
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected ReadGoalDetailDto 유효한_골_dto = new ReadGoalDetailDto(
            유효한_골_아이디,
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_날짜수,
            현재_진행중인_날짜수,
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );
}
