package com.backend.blooming.user.domain;

import com.backend.blooming.user.domain.exception.MemberException;
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
public class Name {

    private static final int MAX_LENGTH = 50;

    @Column(name = "name", unique = true, length = MAX_LENGTH, nullable = false)
    private String value;

    public Name(final String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(final String value) {
        if (value == null || value.isEmpty()) {
            throw new MemberException.NullOrEmptyNameException();
        }
        if (value.length() > MAX_LENGTH) {
            throw new MemberException.LongerThanMaximumNameLengthException();
        }
    }

    public boolean isSame(final Name value) {
        return this.equals(value);
    }
}
