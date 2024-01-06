package com.backend.blooming.friend.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.friend.application.FriendService;
import com.backend.blooming.friend.application.dto.ReadFriendsDto;
import com.backend.blooming.friend.presentation.dto.response.ReadFriendsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping(value = "/{requestedUserId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> request(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestedUserId
    ) {
        friendService.request(authenticatedUser.userId(), requestedUserId);

        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping(value = "/request", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadFriendsResponse> readAllByRequestId(
            @Authenticated final AuthenticatedUser authenticatedUser
    ) {
        final ReadFriendsDto friendsDto = friendService.readAllByRequestId(authenticatedUser.userId());
        final ReadFriendsResponse response = ReadFriendsResponse.from(friendsDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/requested", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadFriendsResponse> readAllByRequestedId(
            @Authenticated final AuthenticatedUser authenticatedUser
    ) {
        final ReadFriendsDto friendsDto = friendService.readAllByRequestedId(authenticatedUser.userId());
        final ReadFriendsResponse response = ReadFriendsResponse.from(friendsDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/mutual", headers = "X-API-VERSION=1")
    public ResponseEntity<ReadFriendsResponse> readAllMutualByUserId(
            @Authenticated final AuthenticatedUser authenticatedUser
    ) {
        final ReadFriendsDto friendsDto = friendService.readAllMutualByUserId(authenticatedUser.userId());
        final ReadFriendsResponse response = ReadFriendsResponse.from(friendsDto);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{requestId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> accept(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestId
    ) {
        friendService.accept(authenticatedUser.userId(), requestId);

        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping(value = "/{requestId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> delete(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @PathVariable final Long requestId
    ) {
        friendService.delete(authenticatedUser.userId(), requestId);

        return ResponseEntity.noContent()
                             .build();
    }
}
