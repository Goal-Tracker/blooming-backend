package com.backend.blooming.goal.presentation;

import com.backend.blooming.authentication.presentation.anotaion.Authenticated;
import com.backend.blooming.authentication.presentation.argumentresolver.AuthenticatedUser;
import com.backend.blooming.goal.application.PokeService;
import com.backend.blooming.goal.application.dto.PokeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class PokeController {

    private final PokeService pokeService;

    @PostMapping(value = "/{goalId}/poke/{userId}", headers = "X-API-VERSION=1")
    public ResponseEntity<Void> poke(
            @Authenticated final AuthenticatedUser authenticatedUser,
            @PathVariable final Long goalId,
            @PathVariable final Long userId
    ) {
        pokeService.poke(PokeDto.of(goalId, authenticatedUser.userId(), userId));

        return ResponseEntity.noContent()
                             .build();
    }
}
