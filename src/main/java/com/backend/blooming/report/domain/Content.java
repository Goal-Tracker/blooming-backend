package com.backend.blooming.report.domain;

import com.backend.blooming.report.domain.exception.InvalidReportException;
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
public class Content {

    @Column(columnDefinition = "text", name = "content", nullable = false)
    private String value;

    public Content(final String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidReportException.NullOrEmptyContentException();
        }
    }
}
