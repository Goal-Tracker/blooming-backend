package com.backend.blooming.admin.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record RequestFriendRequest(
        @NotEmpty(message = "친구 요청자가 입력되지 않았습니다.")
        @Positive(message = "친구 요청자의 아이디가 잘못됐습니다.")
        Long requestUser,

        @NotEmpty(message = "친구 요청 수신자가 입력되지 않았습니다.")
        @Positive(message = "친구 요청 수신자의 아이디가 잘못됐습니다.")
        Long requestedUser
) {
}
