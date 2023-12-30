package com.backend.blooming.themecolor.application;

import com.backend.blooming.themecolor.application.dto.ReadThemeColorDto;
import com.backend.blooming.themecolor.domain.ThemeColor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ThemeColorService {

    public List<ReadThemeColorDto> readAll() {
        return Arrays.stream(ThemeColor.values())
                     .map(ReadThemeColorDto::from)
                     .toList();
    }
}
