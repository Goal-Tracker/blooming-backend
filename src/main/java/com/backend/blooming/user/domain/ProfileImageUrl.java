package com.backend.blooming.user.domain;

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
public class ProfileImageUrl {

    private static final String DEFAULT_IMAGE_URL = "";

    @Column(name = "profile_image_url", nullable = false)
    private String value;

    public ProfileImageUrl(final String value) {
        this.value = processDefaultValue(value);
    }

    private String processDefaultValue(final String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT_IMAGE_URL;
        }

        return value;
    }
}
