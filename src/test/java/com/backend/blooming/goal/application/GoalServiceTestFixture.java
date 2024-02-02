package com.backend.blooming.goal.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.friend.domain.Friend;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.application.dto.UpdateGoalDto;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class GoalServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private FriendRepository friendRepository;

    protected final long 테스트를_위한_시스템_현재_시간_설정값 = 10L;
    protected Long 유효한_사용자_아이디;
    protected Long 골_관리자가_아닌_사용자_아이디;
    protected Long 존재하지_않는_사용자_아이디 = 999L;
    protected String 골_제목 = "골 제목";
    protected String 골_메모 = "골 메모";
    protected LocalDate 골_시작일 = LocalDate.now();
    protected LocalDate 골_종료일 = LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 + 10);
    protected long 골_날짜수 = ChronoUnit.DAYS.between(골_시작일, 골_종료일) + 1;
    protected List<Long> 골_팀에_등록된_사용자_아이디_목록 = new ArrayList<>();
    protected List<Long> 수정_요청한_골_참여자_아이디_목록 = new ArrayList<>();
    protected CreateGoalDto 유효한_골_생성_dto;
    protected Goal 현재_진행중인_골1;
    protected Goal 현재_진행중인_골2;
    protected Goal 이미_종료된_골1;
    protected Goal 이미_종료된_골2;
    protected ReadGoalDetailDto 유효한_골_dto;
    protected CreateGoalDto 존재하지_않는_사용자가_관리자인_골_생성_dto;
    protected CreateGoalDto 친구가_아닌_사용자가_참여자로_있는_골_생성_dto;
    protected CreateGoalDto 골_시작날짜가_현재보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_종료날짜가_현재보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_종료날짜가_시작날짜보다_이전인_골_생성_dto;
    protected CreateGoalDto 골_날짜수가_100_초과인_골_생성_dto;
    protected Long 존재하지_않는_골_아이디 = 997L;
    protected Long 유효한_골_아이디;
    protected List<User> 골_참여_사용자_목록 = new ArrayList<>();
    protected Long 유효하지_않은_골_아이디;
    protected List<Goal> 참여한_골_목록 = new ArrayList<>();
    protected ReadAllGoalDto 사용자가_참여한_골_목록;
    protected UpdateGoalDto 수정_요청한_골_dto;
    protected UpdateGoalDto 골_제목이_비어있는_수정_요청_골_dto;
    protected UpdateGoalDto 골_메모가_비어있는_수정_요청_골_dto;
    protected UpdateGoalDto 골_종료날짜가_비어있는_수정_요청_골_dto;
    protected UpdateGoalDto 골_참여자_목록이_비어있는_수정_요청_골_dto;
    protected String 수정한_제목 = "골 제목 수정 테스트";
    protected String 수정한_메모 = "골 메모 수정 테스트";
    protected LocalDate 수정한_종료날짜 = LocalDate.now().plusDays(50);
    protected User 현재_로그인한_사용자;
    protected User 친구인_사용자;
    protected User 친구가_아닌_사용자;
    protected User 친구인_사용자2;

    @BeforeEach
    void setUp() {
        Long 존재하지_않는_사용자_아이디 = 998L;
        CreateGoalRequest 유효한_골_생성_요청_dto;
        CreateGoalRequest 존재하지_않는_사용자가_관리자인_골_생성_요청_dto;

        현재_로그인한_사용자 = User.builder()
                          .oAuthId("아이디")
                          .oAuthType(OAuthType.KAKAO)
                          .email(new Email("test@gmail.com"))
                          .name(new Name("테스트"))
                          .color(ThemeColor.BABY_BLUE)
                          .statusMessage("상태메시지")
                          .build();
        친구인_사용자 = User.builder()
                      .oAuthId("아이디2")
                      .oAuthType(OAuthType.KAKAO)
                      .email(new Email("test2@gmail.com"))
                      .name(new Name("테스트2"))
                      .color(ThemeColor.BABY_BLUE)
                      .statusMessage("상태메시지2")
                      .build();
        친구가_아닌_사용자 = User.builder()
                         .oAuthId("아이디3")
                         .oAuthType(OAuthType.KAKAO)
                         .email(new Email("test3@gmail.com"))
                         .name(new Name("테스트3"))
                         .color(ThemeColor.CORAL)
                         .statusMessage("상태메시지3")
                         .build();
        친구인_사용자2 = User.builder()
                       .oAuthId("아이디4")
                       .oAuthType(OAuthType.KAKAO)
                       .email(new Email("test4@gmail.com"))
                       .name(new Name("테스트4"))
                       .color(ThemeColor.INDIGO)
                       .statusMessage("상태메시지4")
                       .build();

        userRepository.saveAll(List.of(현재_로그인한_사용자, 친구인_사용자, 친구가_아닌_사용자, 친구인_사용자2));
        유효한_사용자_아이디 = 현재_로그인한_사용자.getId();
        골_관리자가_아닌_사용자_아이디 = 친구인_사용자.getId();
        수정_요청한_골_참여자_아이디_목록.addAll(List.of(유효한_사용자_아이디, 골_관리자가_아닌_사용자_아이디, 친구인_사용자2.getId()));

        final Friend 유효한_친구 = new Friend(현재_로그인한_사용자, 친구인_사용자);
        final Friend 유효한_친구2 = new Friend(현재_로그인한_사용자, 친구인_사용자2);
        friendRepository.saveAll(List.of(유효한_친구, 유효한_친구2));
        유효한_친구.acceptRequest();
        유효한_친구2.acceptRequest();

        골_참여_사용자_목록.addAll(List.of(현재_로그인한_사용자, 친구인_사용자));

        현재_진행중인_골1 = Goal.builder()
                         .name(골_제목)
                         .memo(골_메모)
                         .startDate(골_시작일)
                         .endDate(골_종료일)
                         .managerId(유효한_사용자_아이디)
                         .users(골_참여_사용자_목록)
                         .build();
        현재_진행중인_골2 = Goal.builder()
                         .name("골 제목2")
                         .memo("골 메모2")
                         .startDate(골_시작일)
                         .endDate(LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 + 1))
                         .managerId(유효한_사용자_아이디)
                         .users(골_참여_사용자_목록)
                         .build();
        이미_종료된_골1 = Goal.builder()
                        .name("이미 종료된 골1")
                        .memo("이미 종료된 골1")
                        .startDate(골_시작일)
                        .endDate(LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 - 1))
                        .managerId(유효한_사용자_아이디)
                        .users(골_참여_사용자_목록)
                        .build();
        이미_종료된_골2 = Goal.builder()
                        .name("이미 종료된 골2")
                        .memo("이미 종료된 골2")
                        .startDate(골_시작일)
                        .endDate(LocalDate.now().plusDays(테스트를_위한_시스템_현재_시간_설정값 - 2))
                        .managerId(유효한_사용자_아이디)
                        .users(골_참여_사용자_목록)
                        .build();

        goalRepository.saveAll(List.of(현재_진행중인_골1, 현재_진행중인_골2, 이미_종료된_골1, 이미_종료된_골2));
        유효한_골_아이디 = 현재_진행중인_골1.getId();

        골_팀에_등록된_사용자_아이디_목록.addAll(List.of(현재_로그인한_사용자.getId(), 친구인_사용자.getId()));
        List<Long> 친구가_아닌_사용자가_포함된_사용자_아이디_목록 = new ArrayList<>(List.of(현재_로그인한_사용자.getId(), 친구가_아닌_사용자.getId()));
        참여한_골_목록.addAll(List.of(현재_진행중인_골1, 현재_진행중인_골2, 이미_종료된_골1, 이미_종료된_골2));

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

        친구가_아닌_사용자가_참여자로_있는_골_생성_dto = new CreateGoalDto(
                골_제목,
                골_메모,
                골_시작일,
                골_종료일,
                유효한_사용자_아이디,
                친구가_아닌_사용자가_포함된_사용자_아이디_목록
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

        유효한_골_dto = ReadGoalDetailDto.from(현재_진행중인_골1);

        사용자가_참여한_골_목록 = ReadAllGoalDto.from(참여한_골_목록);

        수정_요청한_골_dto = new UpdateGoalDto(
                수정한_제목,
                수정한_메모,
                수정한_종료날짜,
                수정_요청한_골_참여자_아이디_목록
        );
        골_제목이_비어있는_수정_요청_골_dto = new UpdateGoalDto(
                "",
                수정한_메모,
                수정한_종료날짜,
                수정_요청한_골_참여자_아이디_목록
        );
        골_메모가_비어있는_수정_요청_골_dto = new UpdateGoalDto(
                수정한_제목,
                "",
                수정한_종료날짜,
                수정_요청한_골_참여자_아이디_목록
        );
        골_종료날짜가_비어있는_수정_요청_골_dto = new UpdateGoalDto(
                수정한_제목,
                수정한_메모,
                null,
                수정_요청한_골_참여자_아이디_목록
        );
        골_참여자_목록이_비어있는_수정_요청_골_dto = new UpdateGoalDto(
                수정한_제목,
                수정한_메모,
                수정한_종료날짜,
                new ArrayList<>()
        );
    }
}
