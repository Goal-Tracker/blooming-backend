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
    public ResponseEntity<CreateGoalResponse> createGoal(@RequestPart(required = false) @Valid CreateGoalRequest request) {
        try{
            final CreateGoalDto createGoalDto = goalService.createGoalDto(request);
            final CreateGoalResponse response = goalService.createGoalResponse(createGoalDto);

            return ResponseEntity.created(URI.create("/goals/"+response.goalId())).body(response);
        } catch (ParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}