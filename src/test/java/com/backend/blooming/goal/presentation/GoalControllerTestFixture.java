package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;
import com.backend.blooming.goal.presentation.dto.response.ReadAllGoalResponse;
import com.backend.blooming.goal.presentation.dto.response.ReadGoalResponse;
import com.backend.blooming.themecolor.domain.ThemeColor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalControllerTestFixture {

    private String 골_제목 = "골 제목";
    private String 골_메모 = "골 메모";
    private LocalDate 골_시작일 = LocalDate.now();
    private LocalDate 골_종료일 = LocalDate.now().plusDays(40);
    private Long 골_관리자_아이디 = 1L;
    private List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>(List.of(1L, 2L, 3L));
    private List<Long> 친구가_아닌_사용자가_있는_사용자_아이디_목록 = new ArrayList<>(List.of(골_관리자_아이디, 999L));
    private List<Long> 유효하지_않은_골_참여_사용자_아이디_목록 = new ArrayList<>(List.of(1L, 2L, 3L, 4L, 5L, 6L));
    protected AuthClaims 사용자_토큰_정보 = new AuthClaims(골_관리자_아이디);
    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected Long 유효한_골_아이디 = 1L;
    protected CreateGoalRequest 유효한_골_생성_요청_dto = new CreateGoalRequest(
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected CreateGoalDto 유효한_골_생성_dto = CreateGoalDto.of(유효한_골_생성_요청_dto, 골_관리자_아이디);

    protected CreateGoalRequest 친구가_아닌_사용자가_참여자로_있는_골_생성_요청_dto = new CreateGoalRequest(
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            친구가_아닌_사용자가_있는_사용자_아이디_목록
    );

    protected CreateGoalDto 친구가_아닌_사용자가_참여자로_있는_골_생성_dto = CreateGoalDto.of(친구가_아닌_사용자가_참여자로_있는_골_생성_요청_dto, 골_관리자_아이디);
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
    protected CreateGoalDto 참여자_리스트가_5명_초과인_골_생성_dto = new CreateGoalDto(
            골_제목,
            골_메모,
            LocalDate.now(),
            LocalDate.now().plusDays(40),
            골_관리자_아이디,
            유효하지_않은_골_참여_사용자_아이디_목록
    );
    protected CreateGoalRequest 요청한_골_dto = new CreateGoalRequest(
            골_제목,
            골_메모,
            골_시작일,
            골_종료일,
            골_팀에_등록된_사용자_아이디_목록
    );

    protected ReadGoalDetailDto.GoalTeamWithUserInfoDto 골_참여자1 = new ReadGoalDetailDto.GoalTeamWithUserInfoDto(1L, "테스트 유저1", ThemeColor.BABY_PINK, "테스트 상태메시지1");
    protected ReadGoalDetailDto.GoalTeamWithUserInfoDto 골_참여자2 = new ReadGoalDetailDto.GoalTeamWithUserInfoDto(2L, "테스트 유저2", ThemeColor.BABY_BLUE, "테스트 상태메시지2");
    protected ReadGoalResponse.GoalTeamWithUserInfoResponse 골_참여자_응답1 = new ReadGoalResponse.GoalTeamWithUserInfoResponse(1L, "테스트 유저1", "#f8c8c4", "테스트 상태메시지1");
    protected ReadGoalResponse.GoalTeamWithUserInfoResponse 골_참여자_응답2 = new ReadGoalResponse.GoalTeamWithUserInfoResponse(2L, "테스트 유저2", "#a1b3d7", "테스트 상태메시지2");
    protected ReadGoalDetailDto 유효한_골_dto = new ReadGoalDetailDto(1L, "테스트 골1", "테스트 골 메모1", LocalDate.now(), LocalDate.now().plusDays(10), 11, 1L, List.of(골_참여자1, 골_참여자2));
    protected ReadGoalResponse 유효한_골_응답_dto = new ReadGoalResponse(1L, "테스트 골1", "테스트 골 메모1", LocalDate.now(), LocalDate.now().plusDays(10), 11, 1L, List.of(골_참여자_응답1, 골_참여자_응답2));
    protected ReadAllGoalDto.GoalInfoDto.GoalTeamWithUserInfoDto 골_참여자_정보1 = new ReadAllGoalDto.GoalInfoDto.GoalTeamWithUserInfoDto(1L, "테스트 유저1", ThemeColor.BABY_PINK);
    protected ReadAllGoalDto.GoalInfoDto.GoalTeamWithUserInfoDto 골_참여자_정보2 = new ReadAllGoalDto.GoalInfoDto.GoalTeamWithUserInfoDto(2L, "테스트 유저2", ThemeColor.BABY_BLUE);
    protected ReadAllGoalDto.GoalInfoDto 진행중인_골_정보_dto = new ReadAllGoalDto.GoalInfoDto(1L, "테스트 골1", LocalDate.now(), LocalDate.now().plusDays(10), 11, List.of(골_참여자_정보1, 골_참여자_정보2));
    protected ReadAllGoalDto.GoalInfoDto 종료된_골_정보_dto = new ReadAllGoalDto.GoalInfoDto(2L, "테스트 골2", LocalDate.now(), LocalDate.now().plusDays(5), 6, List.of(골_참여자_정보1, 골_참여자_정보2));
    protected ReadAllGoalResponse.GoalTeamWithUserInfoResponse 골_참여자_정보_응답1 = new ReadAllGoalResponse.GoalTeamWithUserInfoResponse(1L, "테스트 유저1", "#f8c8c4");
    protected ReadAllGoalResponse.GoalTeamWithUserInfoResponse 골_참여자_정보_응답2 = new ReadAllGoalResponse.GoalTeamWithUserInfoResponse(2L, "테스트 유저2", "#a1b3d7");
    protected ReadAllGoalResponse.GoalInfoResponse 진행중인_골_정보_응답_dto = new ReadAllGoalResponse.GoalInfoResponse(1L, "테스트 골1", LocalDate.now(), LocalDate.now().plusDays(10), 11, List.of(골_참여자_정보_응답1, 골_참여자_정보_응답2));
    protected ReadAllGoalResponse.GoalInfoResponse 종료된_골_정보_응답_dto = new ReadAllGoalResponse.GoalInfoResponse(2L, "테스트 골2", LocalDate.now(), LocalDate.now().plusDays(5), 6, List.of(골_참여자_정보_응답1, 골_참여자_정보_응답2));
    protected ReadAllGoalDto 사용자가_참여한_현재_진행중인_골_목록_dto = new ReadAllGoalDto(List.of(진행중인_골_정보_dto));
    protected ReadAllGoalDto 사용자가_참여한_종료된_골_목록_dto = new ReadAllGoalDto(List.of(종료된_골_정보_dto));
    protected ReadAllGoalResponse 사용자가_참여한_현재_진행중인_골_목록_응답_dto = new ReadAllGoalResponse(List.of(진행중인_골_정보_응답_dto));
    protected ReadAllGoalResponse 사용자가_참여한_종료된_골_목록_응답_dto = new ReadAllGoalResponse(List.of(종료된_골_정보_응답_dto));
}
