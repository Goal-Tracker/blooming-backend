package com.backend.blooming.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String color;

    @Column(columnDefinition = "text")
    private String statusMessage;

    @Builder
    public User(
            final String oauthId,
            final String email,
            final String name,
            final String color,
            final String statusMessage
    ) {
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.color = color;
        this.statusMessage = statusMessage;
    }
}
