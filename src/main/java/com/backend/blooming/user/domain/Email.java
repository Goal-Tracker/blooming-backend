package com.backend.blooming.user.domain;

import com.backend.blooming.user.domain.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Email {

    private static final int MAX_LENGTH = 50;
    private static final String PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Column(name = "email", length = MAX_LENGTH, nullable = false)
    private String value;

    public Email(final String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(final String value) {
        if (value == null || value.isBlank()) {
            throw new MemberException.NullOrEmptyEmailException();
        }
        if (value.length() > MAX_LENGTH) {
            throw new MemberException.LongerThanMaximumEmailLengthException();
        }
        if (!Pattern.matches(PATTERN, value)) {
            throw new MemberException.InvalidEmailFormatException();
        }
    }
}
