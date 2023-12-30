package com.backend.blooming.themecolor.presentation;

import com.backend.blooming.themecolor.application.ThemeColorService;
import com.backend.blooming.themecolor.application.dto.ReadThemeColorDto;
import com.backend.blooming.themecolor.presentation.dto.response.ReadThemeColorsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/theme-colors")
@RequiredArgsConstructor
public class ThemeColorController {

    private final ThemeColorService themeColorService;

    @GetMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<ReadThemeColorsResponse> readAll() {
        final List<ReadThemeColorDto> readThemeColorDtos = themeColorService.readAll();

        return ResponseEntity.ok(ReadThemeColorsResponse.from(readThemeColorDtos));
    }
}
