package com.backend.blooming.friend.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.friend.application.FriendService;
import com.backend.blooming.friend.application.dto.ReadRequestFriendsDto;
import com.backend.blooming.friend.application.dto.ReadRequestedFriendsDto;
import com.backend.blooming.friend.presentation.response.ReadRequestFriendsResponse;
import com.backend.blooming.friend.presentation.response.ReadRequestedFriendsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/request", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadRequestedFriendsResponse> readAllByRequestId(
            @Authenticated AuthenticatedUser authenticatedUser
    ) {
        final ReadRequestedFriendsDto friendsDto = friendService.readAllByRequestId(authenticatedUser.userId());
        final ReadRequestedFriendsResponse response = ReadRequestedFriendsResponse.from(friendsDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/requested", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadRequestFriendsResponse> readAllByRequestedId(
            @Authenticated AuthenticatedUser authenticatedUser
    ) {
        final ReadRequestFriendsDto friendsDto = friendService.readAllByRequestedId(authenticatedUser.userId());
        final ReadRequestFriendsResponse response = ReadRequestFriendsResponse.from(friendsDto);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{requestId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> accept(
            @Authenticated AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestId
    ) {
        friendService.accept(authenticatedUser.userId(), requestId);

        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping(value = "/{requestId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> delete(
            @Authenticated AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestId
    ) {
        friendService.delete(authenticatedUser.userId(), requestId);

        return ResponseEntity.noContent()
                             .build();
    }
}
