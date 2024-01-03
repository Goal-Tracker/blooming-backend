package com.backend.blooming.themecolor.presentation.dto.response;

import com.backend.blooming.themecolor.application.dto.ReadThemeColorsDto;

import java.util.List;

public record ReadThemeColorsResponse(List<ReadThemeColorResponse> themeColors) {
    public static ReadThemeColorsResponse from(final ReadThemeColorsDto readThemeColorDtos) {
        final List<ReadThemeColorResponse> themeColors = readThemeColorDtos.colors()
                                                                           .stream()
                                                                           .map(ReadThemeColorResponse::from)
                                                                           .toList();

        return new ReadThemeColorsResponse(themeColors);
    }

    public record ReadThemeColorResponse(String name, String code) {

        // TODO: 1/3/24 [고민] inner class임을 명확히 하기 위해 클래스를 체이닝하는 것이 좋을까요? 혹은 실제 사용하는 클래스만 남기고 static import 하는 것이 좋을까요?
        public static ReadThemeColorResponse from(final ReadThemeColorsDto.ReadThemeColorDto readThemeColorDto) {
            return new ReadThemeColorResponse(readThemeColorDto.name(), readThemeColorDto.code());
        }
    }
}
