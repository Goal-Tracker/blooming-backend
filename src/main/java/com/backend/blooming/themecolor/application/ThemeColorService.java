package com.backend.blooming.themecolor.application;

import com.backend.blooming.themecolor.application.dto.ReadThemeColorsDto;
import com.backend.blooming.themecolor.domain.ThemeColor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeColorService {

    public ReadThemeColorsDto readAll() {
        return ReadThemeColorsDto.from(List.of(ThemeColor.values()));
    }
}
