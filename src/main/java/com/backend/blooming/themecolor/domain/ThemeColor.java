package com.backend.blooming.themecolor.domain;

import com.backend.blooming.themecolor.domain.exception.UnsupportedThemeColorException;
import lombok.Getter;

@Getter
public enum ThemeColor {

    BEIGE("f5f1f0"),
    BABY_PINK("f8c8c4"),
    LIGHT_PINK("ebc0c7"),
    CORAL("f69b94"),
    LIGHT_CORAL("f2a4b1"),
    LIGHT_ORANGE("fcdcce"),
    LIGHT_GREEN("d5e3e6"),
    MINT("aad3d7"),
    TURQUOISE("7bb6c8"),
    BLUE("96b0e5"),
    BABY_BLUE("a1b3d7"),
    LIGHT_BLUE("92b9e2"),
    INDIGO("7175a5"),
    PURPLE("bd83cf"),
    LIGHT_PURPLE("e5afe9");

    private static final String CODE_TAG = "#";

    private final String code;

    ThemeColor(final String code) {
        this.code = CODE_TAG + code;
    }

    public static ThemeColor from(final String themeColor) {
        try {
            return ThemeColor.valueOf(themeColor.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new UnsupportedThemeColorException();
        }
    }
}
