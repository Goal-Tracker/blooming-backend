package com.backend.blooming.goal.application.fixture;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class GoalServiceFixture {

    protected CreateGoalDto 유효한_골_생성_dto;

    @BeforeEach
    void setUp() {
        유효한_골_생성_dto = new CreateGoalDto(
                "제목",
                "내용",
                "2023-11-05",
                "2024-01-03",
                60
        );
    }
}
