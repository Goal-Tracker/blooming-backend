package com.backend.blooming.goal.application.fixture;

import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoalServiceFixture {

    @Autowired
    private GoalRepository goalRepository;

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
