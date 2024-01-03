package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.presentation.dto.request.GoalRequest;
import com.backend.blooming.goal.presentation.dto.response.GoalResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalControllerTestFixture {

    private String 골_제목 = "골 제목";
    private String 골_메모 = "골 메모";
    private LocalDate 골_시작일 = LocalDate.now();
    private LocalDate 골_종료일 = LocalDate.now().plusDays(40);
    private long 골_날짜수 = 40L;
    private Long 골_관리자_아이디 = 1L;
    private List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>(List.of(1L, 2L, 3L));

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

    protected GoalRequest 요청한_골_dto = new GoalRequest(
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected GoalResponse 응답한_골_dto = new GoalResponse(
            유효한_골_아이디,
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_날짜수,
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected GoalDto 유효한_골_dto = new GoalDto(
            유효한_골_아이디,
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_날짜수,
            골_관리자_아이디,
            골_팀에_등록된_사용자_아이디_목록
    );
}
