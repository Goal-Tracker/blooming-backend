package com.backend.blooming.goal.application.fixture;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.domain.GoalTeam;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
public class GoalServiceFixture {

    @Autowired
    private UserRepository userRepository;

    protected Long userId;
    protected Long userId2;
    protected CreateGoalDto 유효한_골_생성_dto;
    protected List<Long> goalTeamUserIds = new ArrayList<>();
    protected List<GoalTeam> goalTeams = new ArrayList<>();

    @BeforeEach
    void setUp() {
        final User user = User.builder()
                .oauthId("아이디")
                .email("test@gmail.com")
                .name("테스트")
                .build();

        final User user2 = User.builder()
                .oauthId("아이디2")
                .email("test2@gmail.com")
                .name("테스트2")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        userId = user.getId();
        userId2 = user2.getId();

        goalTeamUserIds.add(userId);
        goalTeamUserIds.add(userId2);

        유효한_골_생성_dto = new CreateGoalDto(
                "제목",
                "내용",
                "2023-11-05",
                "2024-01-03",
                60,
                goalTeamUserIds
        );
    }
}
