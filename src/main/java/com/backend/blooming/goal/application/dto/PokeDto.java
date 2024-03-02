package com.backend.blooming.goal.application.dto;

public record PokeDto(Long goalId, Long senderId, Long receiverId) {

    public static PokeDto of(final Long goalId, final Long senderId, final Long receiverId) {
        return new PokeDto(goalId, senderId, receiverId);
    }
}
