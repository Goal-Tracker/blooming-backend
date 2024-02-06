package com.backend.blooming.admin.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record RequestFriendRequest(
        @NotEmpty
        @Positive
        Long requestUser,

        @NotEmpty
        @Positive
        Long requestedUser
) {
}
