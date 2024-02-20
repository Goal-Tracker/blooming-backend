package com.backend.blooming.stamp.domain;

import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Message {

    private static final int STAMP_MESSAGE_MAXIMUM = 30;

    @Column(columnDefinition = "text", nullable = false, length = STAMP_MESSAGE_MAXIMUM)
    private String message;

    public Message(final String message) {
        this.message = validateMessage(message);
    }

    private String validateMessage(final String message) {
        if (message == null || message.isEmpty()) {
            throw new InvalidStampException.InvalidStampMessage();
        }
        if (message.length() > STAMP_MESSAGE_MAXIMUM) {
            throw new InvalidStampException.InvalidStampMessage();
        }

        return message;
    }
}
