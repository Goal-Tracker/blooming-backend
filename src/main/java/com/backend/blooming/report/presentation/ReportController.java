package com.backend.blooming.report.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.report.application.GoalReportService;
import com.backend.blooming.report.application.StampReportService;
import com.backend.blooming.report.application.UserReportService;
import com.backend.blooming.report.application.dto.CreateGoalReportDto;
import com.backend.blooming.report.application.dto.CreateStampReportDto;
import com.backend.blooming.report.application.dto.CreateUserReportDto;
import com.backend.blooming.report.presentation.dto.request.CreateReportRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final UserReportService userReportService;
    private final GoalReportService goalReportService;
    private final StampReportService stampReportService;

    @PostMapping(value = "/users/{userId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> createUserReport(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @PathVariable final Long userId,
            @RequestBody @Valid final CreateReportRequest request
    ) {
        final CreateUserReportDto userReportDto = CreateUserReportDto.of(authenticatedUser.userId(), userId, request);
        userReportService.create(userReportDto);

        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping(value = "/goals/{goalId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> createGoalReport(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @PathVariable final Long goalId,
            @RequestBody @Valid final CreateReportRequest request
    ) {
        final CreateGoalReportDto goalReportDto = CreateGoalReportDto.of(authenticatedUser.userId(), goalId, request);
        goalReportService.create(goalReportDto);

        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping(value = "/stamps/{stampId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> createStampReport(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @PathVariable final Long stampId,
            @RequestBody @Valid final CreateReportRequest request
    ) {
        final CreateStampReportDto stampReportDto = CreateStampReportDto.of(
                authenticatedUser.userId(),
                stampId,
                request
        );
        stampReportService.create(stampReportDto);

        return ResponseEntity.noContent()
                             .build();
    }
}
