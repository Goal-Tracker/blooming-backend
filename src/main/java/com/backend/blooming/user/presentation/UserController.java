package com.backend.blooming.user.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.user.application.UserService;
import com.backend.blooming.user.application.dto.ReadUsersWithFriendsStatusDto;
import com.backend.blooming.user.application.dto.UpdateUserDto;
import com.backend.blooming.user.application.dto.ReadUserDto;
import com.backend.blooming.user.presentation.dto.request.UpdateUserRequest;
import com.backend.blooming.user.presentation.dto.response.ReadUsersWithFriendsStatusResponse;
import com.backend.blooming.user.presentation.dto.response.ReadUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<ReadUserResponse> readById(@Authenticated AuthenticatedUser authenticatedUser) {
        final ReadUserDto readUserDto = userService.readById(authenticatedUser.userId());

        return ResponseEntity.ok(ReadUserResponse.from(readUserDto));
    }

    @GetMapping(value = "/all", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadUsersWithFriendsStatusResponse> readAllWithKeyword(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @RequestParam(required = false, defaultValue = "") final String keyword
    ) {
        final ReadUsersWithFriendsStatusDto usersDto = userService.readAllWithKeyword(
                authenticatedUser.userId(),
                keyword
        );

        return ResponseEntity.ok(ReadUsersWithFriendsStatusResponse.from(usersDto));
    }

    @PatchMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<ReadUserResponse> updateById(
            @Authenticated AuthenticatedUser authenticatedUser,
            @RequestBody UpdateUserRequest updateUserRequest
    ) {
        final ReadUserDto readUserDto = userService.updateById(
                authenticatedUser.userId(),
                UpdateUserDto.from(updateUserRequest)
        );

        return ResponseEntity.ok(ReadUserResponse.from(readUserDto));
    }
}
