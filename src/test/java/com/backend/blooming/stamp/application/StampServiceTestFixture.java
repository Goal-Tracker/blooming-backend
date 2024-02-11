package com.backend.blooming.stamp.application;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.domain.Stamp;
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
    protected CreateStampDto 유효한_스탬프_dto;
    protected CreateStampDto 존재하지_않는_사용자가_생성한_스탬프_dto;
    protected CreateStampDto 존재하지_않는_골에서_생성된_스탬프_dto;
    protected CreateStampDto 골_참여자가_아닌_사용자가_생성한_스탬프_dto;
    protected CreateStampDto 이미_존재하는_스탬프_dto;
    
    @BeforeEach
    void setUp() {
        User 스탬프를_생성한_사용자 = User.builder()
                                .oAuthId("아이디")
                                .oAuthType(OAuthType.KAKAO)
                                .email(new Email("test@gmail.com"))
                                .name(new Name("테스트"))
                                .color(ThemeColor.BABY_BLUE)
                                .statusMessage("상태메시지")
                                .build();
        User 골_참여자가_아닌_사용자 = User.builder()
                                 .oAuthId("아이디2")
                                 .oAuthType(OAuthType.KAKAO)
                                 .email(new Email("test2@gmail.com"))
                                 .name(new Name("테스트2"))
                                 .color(ThemeColor.BABY_BLUE)
                                 .statusMessage("상태메시지2")
                                 .build();
        userRepository.saveAll(List.of(스탬프를_생성한_사용자, 골_참여자가_아닌_사용자));
        Goal 스탬프를_생성할_골 = Goal.builder()
                              .name("골 제목")
                              .memo("골 메모")
                              .startDate(LocalDate.now())
                              .endDate(LocalDate.now().plusDays(19))
                              .managerId(스탬프를_생성한_사용자.getId())
                              .users(List.of(스탬프를_생성한_사용자))
                              .build();
        goalRepository.save(스탬프를_생성할_골);
        유효한_스탬프_dto = new CreateStampDto(
                스탬프를_생성할_골.getId(),
                스탬프를_생성한_사용자.getId(),
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
                스탬프를_생성한_사용자.getId(),
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
                스탬프를_생성한_사용자.getId(),
                1,
                "스탬프 메시지"
        );
    }
}
