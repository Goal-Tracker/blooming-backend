package com.backend.blooming.user.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.user.application.UserService;
import com.backend.blooming.user.application.dto.UpdateUserDto;
import com.backend.blooming.user.application.dto.UserDto;
import com.backend.blooming.user.presentation.request.UpdateUserRequest;
import com.backend.blooming.user.presentation.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<UserResponse> readById(@Authenticated AuthenticatedUser authenticatedUser) {
        final UserDto userDto = userService.readById(authenticatedUser.userId());

        return ResponseEntity.ok(UserResponse.from(userDto));
    }

    @PatchMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<UserResponse> updateById(
            @Authenticated AuthenticatedUser authenticatedUser,
            @RequestBody UpdateUserRequest updateUserRequest
    ) {
        final UserDto userDto = userService.updateById(
                authenticatedUser.userId(),
                UpdateUserDto.from(updateUserRequest)
        );

        return ResponseEntity.ok(UserResponse.from(userDto));
    }
}
