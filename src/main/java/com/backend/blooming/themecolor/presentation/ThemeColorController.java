package com.backend.blooming.themecolor.presentation;

import com.backend.blooming.themecolor.application.ThemeColorService;
import com.backend.blooming.themecolor.application.dto.ThemeColorDto;
import com.backend.blooming.themecolor.presentation.response.ThemeColorsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/theme-color")
@RequiredArgsConstructor
public class ThemeColorController {

    private final ThemeColorService themeColorService;

    @GetMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<ThemeColorsResponse> readAll() {
        final List<ThemeColorDto> themeColorDtos = themeColorService.readAll();

        return ResponseEntity.ok(ThemeColorsResponse.from(themeColorDtos));
    }
}
