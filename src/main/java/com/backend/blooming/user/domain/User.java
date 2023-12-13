package com.backend.blooming.user.domain;

import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.common.entity.BaseTimeEntity;
import com.backend.blooming.themecolor.domain.ThemeColor;
import jakarta.persistence.Column;
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

@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id", nullable = false, unique = true)
    private String oAuthId;

    @Column(name = "oauth_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthType oAuthType;

    @Column(nullable = false)
    private String email;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_color")
    private ThemeColor color;

    @Column(columnDefinition = "text")
    private String statusMessage;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Builder
    private User(
            final String oAuthId,
            final OAuthType oAuthType,
            final String email,
            final String name,
            final ThemeColor color,
            final String statusMessage
    ) {
        this.oAuthId = oAuthId;
        this.oAuthType = oAuthType;
        this.email = email;
        this.name = name;
        this.color = color;
        this.statusMessage = statusMessage;
    }

    public void delete() {
        this.deleted = true;
    }

    public void updateName(final String name) {
        this.name = name;
    }

    public void updateColor(final ThemeColor color) {
        this.color = color;
    }

    public void updateStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
