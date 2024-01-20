package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.ReadAllGoalDto;
import com.backend.blooming.goal.application.dto.ReadGoalDetailDto;
import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;
import com.backend.blooming.goal.presentation.dto.response.ReadAllGoalResponse;
import com.backend.blooming.goal.presentation.dto.response.ReadGoalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<ReadGoalResponse> createGoal(@RequestBody @Valid final CreateGoalRequest request,
                                                       @Authenticated final AuthenticatedUser authenticatedUser) {
        final CreateGoalDto createGoalDto = CreateGoalDto.of(request, authenticatedUser.userId());
        final Long goalId = goalService.createGoal(createGoalDto);

        return ResponseEntity.created(URI.create("/goals/" + goalId)).build();
    }

    @GetMapping(value = "/{goalId}", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadGoalResponse> readGoalById(@PathVariable("goalId") final Long goalId) {
        final ReadGoalDetailDto readGoalDetailDto = goalService.readGoalDetailById(goalId);
        final ReadGoalResponse response = ReadGoalResponse.from(readGoalDetailDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/all", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadAllGoalResponse> readAllGoalUserAttend(@Authenticated final AuthenticatedUser authenticatedUser) {
        final ReadAllGoalDto readAllGoalDtos = goalService.readAllGoalByUserId(authenticatedUser.userId());
        final ReadAllGoalResponse response = ReadAllGoalResponse.from(readAllGoalDtos);

        return ResponseEntity.ok().body(response);
    }
}
