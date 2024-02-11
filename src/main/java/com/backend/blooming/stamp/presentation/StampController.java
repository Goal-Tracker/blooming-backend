package com.backend.blooming.stamp.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.stamp.application.StampService;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.presentation.dto.request.CreateStampRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stamps")
@RequiredArgsConstructor
public class StampController {

    private final StampService stampService;

    @PostMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<Void> createStamp(
            @RequestBody @Valid final CreateStampRequest request,
            @Authenticated final AuthenticatedUser authenticatedUser
    ) {
        final CreateStampDto createStampDto = CreateStampDto.of(request, authenticatedUser.userId());
        stampService.createStamp(createStampDto);

        return ResponseEntity.noContent().build();
    }
}
