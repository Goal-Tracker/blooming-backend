package com.backend.blooming.goal.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
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

    protected Long 유효한_사용자_아이디;
    protected String 골_제목 = "골 제목";
    protected String 골_메모 = "골 메모";
    protected LocalDate 골_시작일 = LocalDate.now();
    protected LocalDate 골_종료일 = LocalDate.now().plusDays(40);
    protected long 골_날짜수 = 41L;
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
    protected List<User> 유효한_사용자_목록 = new ArrayList<>();
    protected List<Goal> 참여한_골_목록 = new ArrayList<>();
    protected ReadAllGoalDto 사용자가_참여한_골_목록;

    @BeforeEach
    void setUp() {
        User 유효한_사용자;
        User 유효한_사용자_2;
        Long 존재하지_않는_사용자_아이디 = 998L;
        List<Long> 존재하지_않는_사용자가_있는_사용자_아이디_목록 = new ArrayList<>();
        CreateGoalRequest 유효한_골_생성_요청_dto;
        CreateGoalRequest 존재하지_않는_사용자가_관리자인_골_생성_요청_dto;

        유효한_사용자 = User.builder()
                      .oAuthId("아이디")
                      .oAuthType(OAuthType.KAKAO)
                      .email(new Email("test@gmail.com"))
                      .name(new Name("테스트"))
                      .color(ThemeColor.BABY_BLUE)
                      .statusMessage("상태메시지")
                      .build();

        유효한_사용자_2 = User.builder()
                        .oAuthId("아이디2")
                        .oAuthType(OAuthType.KAKAO)
                        .email(new Email("test2@gmail.com"))
                        .name(new Name("테스트2"))
                        .color(ThemeColor.BABY_BLUE)
                        .statusMessage("상태메시지2")
                        .build();

        userRepository.saveAll(List.of(유효한_사용자, 유효한_사용자_2));
        유효한_사용자_목록.addAll(List.of(유효한_사용자, 유효한_사용자_2));
        유효한_사용자_아이디 = 유효한_사용자.getId();

        유효한_골 = Goal.builder()
                    .name(골_제목)
                    .memo(골_메모)
                    .startDate(골_시작일)
                    .endDate(골_종료일)
                    .managerId(유효한_사용자_아이디)
                    .users(유효한_사용자_목록)
                    .build();

        유효한_골2 = Goal.builder()
                     .name("골 제목2")
                     .memo("골 메모2")
                     .startDate(골_시작일)
                     .endDate(LocalDate.now().plusDays(30))
                     .managerId(유효한_사용자_아이디)
                     .users(유효한_사용자_목록)
                     .build();

        유효한_골3 = Goal.builder()
                     .name("골 제목3")
                     .memo("골 메모3")
                     .startDate(골_시작일)
                     .endDate(LocalDate.now().plusDays(60))
                     .managerId(유효한_사용자_아이디)
                     .users(유효한_사용자_목록)
                     .build();

        goalRepository.saveAll(List.of(유효한_골, 유효한_골2, 유효한_골3));
        유효한_골_아이디 = 유효한_골.getId();

        골_팀에_등록된_사용자_아이디_목록.addAll(List.of(유효한_사용자.getId(), 유효한_사용자_2.getId()));
        참여한_골_목록.addAll(List.of(유효한_골, 유효한_골2, 유효한_골3));

        유효한_골_생성_요청_dto = new CreateGoalRequest(
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_팀에_등록된_사용자_아이디_목록
        );

        유효한_골_생성_dto = CreateGoalDto.of(유효한_골_생성_요청_dto, 유효한_사용자_아이디);

        존재하지_않는_사용자가_관리자인_골_생성_요청_dto = new CreateGoalRequest(
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                골_팀에_등록된_사용자_아이디_목록
        );

        존재하지_않는_사용자가_관리자인_골_생성_dto = CreateGoalDto.of(존재하지_않는_사용자가_관리자인_골_생성_요청_dto, 존재하지_않는_사용자_아이디);

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

        유효한_골_dto = ReadGoalDetailDto.from(유효한_골);

        사용자가_참여한_골_목록 = ReadAllGoalDto.from(참여한_골_목록);
    }
}
