package com.backend.blooming.stamp.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.stamp.application.StampService;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.application.dto.ReadAllStampDto;
import com.backend.blooming.stamp.application.dto.ReadStampDto;
import com.backend.blooming.stamp.presentation.dto.request.CreateStampRequest;
import com.backend.blooming.stamp.presentation.dto.response.ReadAllStampResponse;
import com.backend.blooming.stamp.presentation.dto.response.ReadStampResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class StampController {

    private final StampService stampService;

    @PostMapping(value = "/{goalId}/stamp", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadStampResponse> createStamp(
            @PathVariable("goalId") final Long goalId,
            @RequestPart @Valid final CreateStampRequest request,
            @RequestPart(required = false) final MultipartFile stampImage,
            @Authenticated final AuthenticatedUser authenticatedUser
    ) {
        final CreateStampDto createStampDto = CreateStampDto.of(
                request, goalId, authenticatedUser.userId(), stampImage
        );
        final ReadStampDto stamp = stampService.createStamp(createStampDto);
        final ReadStampResponse response = ReadStampResponse.from(stamp);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{goalId}/stamps", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadAllStampResponse> readAllStampByGoalId(
            @PathVariable("goalId") final Long goalId,
            @Authenticated AuthenticatedUser authenticatedUser
    ) {
        final ReadAllStampDto readAllStampDto = stampService.readAllByGoalId(goalId, authenticatedUser.userId());
        final ReadAllStampResponse response = ReadAllStampResponse.from(readAllStampDto);

        return ResponseEntity.ok(response);
    }
}
