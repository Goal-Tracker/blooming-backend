package com.backend.blooming.goal.presentation;

import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.goal.presentation.dto.request.CreateGoalRequest;
import com.backend.blooming.goal.presentation.dto.response.CreateGoalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.text.ParseException;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping(value = "/add", headers = "X-API-VERSION=1")
    public ResponseEntity<GoalResponse> createGoal(@RequestPart @Valid GoalRequest request) throws ParseException {
        final CreateGoalDto createGoalDto = CreateGoalDto.from(request);
        final GoalDto goalDto = goalService.createGoal(createGoalDto);
        final GoalResponse goalResponse = GoalResponse.from(goalDto);

        return ResponseEntity.created(URI.create("/goals/" +goalResponse.goalId())).body(goalResponse);
    }

    @DeleteMapping(value = "/delete", headers = "X-API-VERSION=1")
    public ResponseEntity<String> deleteGoal(@RequestPart @Valid GoalRequest request) {
        goalService.deleteGoal(request);
        return ResponseEntity.ok("삭제 완료되었습니다.");
    }
}
