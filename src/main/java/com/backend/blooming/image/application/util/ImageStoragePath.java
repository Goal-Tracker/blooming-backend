package com.backend.blooming.image.application.util;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageStoragePath {

    PROFILE("profile/"),
    STAMP("stamp/");

    private final String path;
}
