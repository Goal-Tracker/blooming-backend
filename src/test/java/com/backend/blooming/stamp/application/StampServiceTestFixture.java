package com.backend.blooming.stamp.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.domain.Day;
import com.backend.blooming.stamp.domain.Message;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.stamp.infrastructure.repository.StampRepository;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class StampServiceTestFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private StampRepository stampRepository;

    protected CreateStampDto 유효한_스탬프_dto;
    protected CreateStampDto 존재하지_않는_사용자가_생성한_스탬프_dto;
    protected CreateStampDto 존재하지_않는_골에서_생성된_스탬프_dto;
    protected CreateStampDto 골_참여자가_아닌_사용자가_생성한_스탬프_dto;
    protected CreateStampDto 이미_존재하는_스탬프_dto;
    protected Long 유효한_골_아이디;
    protected Long 스탬프를_생성한_사용자_아이디1;
    protected Long 스탬프를_생성한_사용자_아이디2;
    protected Long 골_참여자가_아닌_사용자_아이디;
    protected ThemeColor 스탬프를_생성한_사용자_컬러1;
    protected ThemeColor 스탬프를_생성한_사용자_컬러2;
    protected User 스탬프를_생성할_사용자;

    @BeforeEach
    void setUp() {
        User 스탬프를_생성한_사용자1 = User.builder()
                                 .oAuthId("아이디")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test@gmail.com"))
                                 .name(new Name("테스트"))
                                 .color(ThemeColor.BABY_BLUE)
                                 .statusMessage("상태메시지")
                                 .build();
        User 스탬프를_생성한_사용자2 = User.builder()
                                 .oAuthId("아이디2")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test2@gmail.com"))
                                 .name(new Name("테스트2"))
                                 .color(ThemeColor.INDIGO)
                                 .statusMessage("상태메시지2")
                                 .build();
        User 골_참여자가_아닌_사용자 = User.builder()
                                 .oAuthId("아이디3")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test3@gmail.com"))
                                 .name(new Name("테스트3"))
                                 .color(ThemeColor.CORAL)
                                 .statusMessage("상태메시지")
                                 .build();
        스탬프를_생성할_사용자 = User.builder()
                           .oAuthId("아이디4")
                           .oAuthType(OAuthType.KAKAO)
                           .email(new Email("test4@gmail.com"))
                           .name(new Name("테스트4"))
                           .color(ThemeColor.BABY_PINK)
                           .statusMessage("상태메시지")
                           .build();
        userRepository.saveAll(List.of(스탬프를_생성한_사용자1, 스탬프를_생성한_사용자2, 골_참여자가_아닌_사용자, 스탬프를_생성할_사용자));
        스탬프를_생성한_사용자_아이디1 = 스탬프를_생성한_사용자1.getId();
        스탬프를_생성한_사용자_아이디2 = 스탬프를_생성한_사용자2.getId();
        골_참여자가_아닌_사용자_아이디 = 골_참여자가_아닌_사용자.getId();
        스탬프를_생성한_사용자_컬러1 = 스탬프를_생성한_사용자1.getColor();
        스탬프를_생성한_사용자_컬러2 = 스탬프를_생성한_사용자2.getColor();

        Goal 스탬프를_생성할_골 = Goal.builder()
                              .name("골 제목")
                              .memo("골 메모")
                              .startDate(LocalDate.now())
                              .endDate(LocalDate.now().plusDays(19))
                              .managerId(스탬프를_생성한_사용자1.getId())
                              .users(List.of(스탬프를_생성한_사용자1, 스탬프를_생성한_사용자2, 스탬프를_생성할_사용자))
                              .build();
        goalRepository.save(스탬프를_생성할_골);
        유효한_골_아이디 = 스탬프를_생성할_골.getId();

        Stamp 유효한_스탬프1 = Stamp.builder()
                              .goal(스탬프를_생성할_골)
                              .user(스탬프를_생성한_사용자1)
                              .day(new Day(스탬프를_생성할_골.getGoalTerm(), 1))
                              .message(new Message("스탬프 메시지"))
                              .build();
        Stamp 유효한_스탬프2 = Stamp.builder()
                              .goal(스탬프를_생성할_골)
                              .user(스탬프를_생성한_사용자2)
                              .day(new Day(스탬프를_생성할_골.getGoalTerm(), 1))
                              .message(new Message("스탬프 메시지2"))
                              .build();
        stampRepository.saveAll(List.of(유효한_스탬프1, 유효한_스탬프2));

        유효한_스탬프_dto = new CreateStampDto(
                스탬프를_생성할_골.getId(),
                스탬프를_생성할_사용자.getId(),
                1,
                "스탬프 메시지"
        );
        존재하지_않는_사용자가_생성한_스탬프_dto = new CreateStampDto(
                스탬프를_생성할_골.getId(),
                999L,
                1,
                "스탬프 메시지"
        );
        존재하지_않는_골에서_생성된_스탬프_dto = new CreateStampDto(
                999L,
                스탬프를_생성한_사용자1.getId(),
                1,
                "스탬프 메시지"
        );
        골_참여자가_아닌_사용자가_생성한_스탬프_dto = new CreateStampDto(
                스탬프를_생성할_골.getId(),
                골_참여자가_아닌_사용자.getId(),
                1,
                "스탬프 메시지"
        );
        이미_존재하는_스탬프_dto = new CreateStampDto(
                스탬프를_생성할_골.getId(),
                스탬프를_생성한_사용자1.getId(),
                1,
                "스탬프 메시지"
        );
    }
}
