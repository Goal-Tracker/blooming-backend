package com.backend.blooming.friend.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.friend.application.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping(value = "/{requestedUserId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> request(
            @Authenticated AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestedUserId
    ) {
        friendService.request(authenticatedUser.userId(), requestedUserId);

        return ResponseEntity.noContent()
                             .build();
    }

    @PatchMapping(value = "/{requestId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> accept(
            @Authenticated AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestId
    ) {
        friendService.acceptFriend(authenticatedUser.userId(), requestId);

        return ResponseEntity.noContent()
                             .build();
    }
}
