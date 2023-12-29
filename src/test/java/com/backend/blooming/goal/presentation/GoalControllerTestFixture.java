package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.goal.presentation.dto.request.GoalRequest;
import com.backend.blooming.goal.presentation.dto.response.GoalResponse;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalControllerTestFixture {

    @Autowired
    private UserRepository userRepository;

    protected CreateGoalDto 유효한_골_생성_dto;
    protected Goal 유효한_골;
    protected GoalDto 유효한_골_dto;
    protected GoalRequest 요청한_골_dto;
    protected GoalResponse 응답한_골_dto;
    protected TokenType 액세스_토큰_타입 = TokenType.ACCESS;
    protected String 액세스_토큰 = "Bearer access_token";
    protected AuthClaims 사용자_토큰_정보;

    @BeforeEach
    void setUp() {
        Long 골_아이디 = 1L;
        String 골_제목 = "골 제목";
        String 골_메모 = "골 메모";
        LocalDate 골_시작일 = LocalDate.now();
        LocalDate 골_종료일 = LocalDate.now().plusDays(40);
        long 골_날짜수 = 40L;
        Long 골_관리자_아이디;
        List<GoalTeam> 골_팀_목록 = new ArrayList<>();
        List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>(List.of(1L));

        final User user = User.builder()
                              .oAuthId("아이디")
                              .oAuthType(OAuthType.KAKAO)
                              .email("test@gmail.com")
                              .name("테스트")
                              .color(ThemeColor.BABY_BLUE)
                              .statusMessage("상태메시지")
                              .build();

        userRepository.save(user);

        사용자_토큰_정보 = new AuthClaims(user.getId());
        골_관리자_아이디 = user.getId();

        유효한_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                골_시작일.toString(),
                골_종료일.toString(),
                골_관리자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        유효한_골 = Goal.builder()
                    .name(골_제목)
                    .memo(골_메모)
                    .startDate(골_시작일)
                    .endDate(골_종료일)
                    .managerId(골_관리자_아이디)
                    .build();

        final GoalTeam goalTeam = new GoalTeam(user, 유효한_골);
        골_팀_목록.add(goalTeam);

        요청한_골_dto = new GoalRequest(
                골_제목,
                골_메모,
                골_시작일.toString(),
                골_종료일.toString(),
                골_팀에_등록된_사용자_아이디_목록
        );

        응답한_골_dto = new GoalResponse(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일.toString(),
                골_종료일.toString(),
                골_날짜수,
                골_관리자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );

        유효한_골_dto = new GoalDto(
                골_아이디,
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_날짜수,
                골_관리자_아이디,
                골_팀에_등록된_사용자_아이디_목록
        );
    }
}
