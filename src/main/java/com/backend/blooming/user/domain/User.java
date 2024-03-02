package com.backend.blooming.user.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.themecolor.domain.ThemeColor;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Table(name = "users")
public class User extends BaseTimeEntity {

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://dp7ped0142moi.cloudfront.net/default/profile.png";
    private static final String DEFAULT_STATUS_MESSAGE = "";
    private static final ThemeColor DEFAULT_THEME_COLOR = ThemeColor.INDIGO;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id", nullable = false, unique = true)
    private String oAuthId;

    @Column(name = "oauth_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthType oAuthType;

    @Embedded
    private Email email;

    @Embedded
    private Name name;

    @Column(nullable = false)
    private String  profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_color", nullable = false)
    private ThemeColor color;

    @Column(columnDefinition = "text", nullable = false)
    private String statusMessage;

    @Column(name = "has_new_alarm", nullable = false)
    private boolean newAlarm = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Builder
    private User(
            final String oAuthId,
            final OAuthType oAuthType,
            final Email email,
            final Name name,
            final String  profileImageUrl,
            final ThemeColor color,
            final String statusMessage
    ) {
        this.oAuthId = oAuthId;
        this.oAuthType = oAuthType;
        this.email = email;
        this.name = name;
        this.profileImageUrl = processDefaultProfileImageUrl(profileImageUrl);
        this.color = processDefaultColor(color);
        this.statusMessage = processDefaultStatusMessage(statusMessage);
    }

    private String  processDefaultProfileImageUrl(final String profileImageUrl) {
        if (profileImageUrl == null || profileImageUrl.isBlank()) {
            return DEFAULT_PROFILE_IMAGE_URL;
        }

        return profileImageUrl;
    }

    private ThemeColor processDefaultColor(final ThemeColor color) {
        if (color == null) {
            return DEFAULT_THEME_COLOR;
        }

        return color;
    }

    private String processDefaultStatusMessage(final String statusMessage) {
        if (statusMessage == null || statusMessage.isBlank()) {
            return DEFAULT_STATUS_MESSAGE;
        }

        return statusMessage;
    }

    public void delete() {
        this.deleted = true;
    }

    public void updateName(final Name name) {
        this.name = name;
    }

    public void updateProfileImageUrl(final String profileImageUrl) {
        this.profileImageUrl = processDefaultProfileImageUrl(profileImageUrl);
    }

    public void updateColor(final ThemeColor color) {
        this.color = color;
    }

    public void updateStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void updateNewAlarm(final boolean newAlarm) {
        this.newAlarm = newAlarm;
    }

    public boolean isSameName(final Name name) {
        return this.name.isSame(name);
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public String getColorName() {
        return color.name();
    }

    public String getColorCode() {
        return color.getCode();
    }
}
