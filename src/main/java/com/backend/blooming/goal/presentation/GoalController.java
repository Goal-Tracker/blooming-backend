package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.application.dto.GoalDto;
import com.backend.blooming.goal.presentation.dto.request.GoalRequest;
import com.backend.blooming.goal.presentation.dto.response.GoalResponse;
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

    @PostMapping(value = "/add", headers = "X-API-VERSION=1")
    public ResponseEntity<GoalResponse> createGoal(@RequestBody @Valid final GoalRequest request,
                                                   @Authenticated final AuthenticatedUser authenticatedUser) {
        final CreateGoalDto createGoalDto = CreateGoalDto.from(request, authenticatedUser.userId());
        final GoalDto goalDto = goalService.createGoal(createGoalDto);
        final GoalResponse goalResponse = GoalResponse.from(goalDto);

        return ResponseEntity.created(URI.create("/goals/" + goalResponse.id())).build();
    }

    @GetMapping(value = "/{goalId}", headers = "X-API-VERSION=1")
    public ResponseEntity<GoalResponse> readGoalById(@PathVariable("goalId") final Long goalId) {
        final GoalDto goalDto = goalService.readGoalById(goalId);
        final GoalResponse goalResponse = GoalResponse.from(goalDto);

        return ResponseEntity.ok().body(goalResponse);
    }
}
