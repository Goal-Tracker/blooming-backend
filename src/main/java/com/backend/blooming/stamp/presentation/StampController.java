package com.backend.blooming.stamp.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.stamp.application.StampService;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.application.dto.ReadStampDto;
import com.backend.blooming.stamp.application.dto.ReadAllStampDto;
import com.backend.blooming.stamp.presentation.dto.request.CreateStampRequest;
import com.backend.blooming.stamp.presentation.dto.response.ReadStampResponse;
import com.backend.blooming.stamp.presentation.dto.response.ReadAllStampResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<ReadStampResponse> createStamp(
            @RequestBody @Valid final CreateStampRequest request,
            @Authenticated final AuthenticatedUser authenticatedUser
    ) {
        final CreateStampDto createStampDto = CreateStampDto.of(request, authenticatedUser.userId());
        final ReadStampDto stamp = stampService.createStamp(createStampDto);
        final ReadStampResponse response = ReadStampResponse.from(stamp);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{goalId}", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadAllStampResponse> readAllStampByGoalId(
            @PathVariable("goalId") final Long goalId,
            @Authenticated AuthenticatedUser authenticatedUser
    ) {
        final ReadAllStampDto readAllStampDto = stampService.readAllByGoalId(goalId, authenticatedUser.userId());
        final ReadAllStampResponse response = ReadAllStampResponse.from(readAllStampDto);

        return ResponseEntity.ok(response);
    }
}
