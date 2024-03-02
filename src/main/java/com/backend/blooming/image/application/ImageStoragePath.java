package com.backend.blooming.image.application;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageStoragePath {

    PROFILE("profile/");

    private final String path;
}
