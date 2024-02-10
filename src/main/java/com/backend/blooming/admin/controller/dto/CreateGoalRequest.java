package com.backend.blooming.admin.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CreateGoalRequest(
        @NotEmpty(message = "이름이 입력되지 않았습니다.")
        String name,

        String memo,

        @NotNull(message = "시작일이 입력되지 않았습니다.")
        LocalDate startDate,

        @NotNull(message = "종료일이 입력되지 않았습니다.")
        LocalDate endDate,

        @NotNull(message = "골 생성자가 설정되지 않았습니다.")
        @Positive(message = "골 생성자의 아이디가 잘못됐습니다.")
        Long manager,

        @NotNull(message = "팀원이 설정되지 않았습니다.")
        @Positive(message = "팀원의 아이디가 잘못됐습니다.")
        Long team
) {
}
