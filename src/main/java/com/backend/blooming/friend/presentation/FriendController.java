package com.backend.blooming.friend.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.friend.application.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping(value = "/add/{requestedUserId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> create(
            @Authenticated AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestedUserId
    ) {
        friendService.create(authenticatedUser.userId(), requestedUserId);

        return ResponseEntity.created(URI.create("/temp-url"))
                             .build();
    }
}
