package com.backend.blooming.admin.controller;

import com.backend.blooming.admin.application.AdminPageService;
import com.backend.blooming.admin.controller.dto.CreateGoalRequest;
import com.backend.blooming.admin.controller.dto.CreateUserRequest;
import com.backend.blooming.admin.controller.dto.FriendStatus;
import com.backend.blooming.admin.controller.dto.RequestFriendRequest;
import com.backend.blooming.admin.controller.dto.UpdateFriendRequest;
import com.backend.blooming.admin.controller.exception.InvalidFriendStatusException;
import com.backend.blooming.friend.application.FriendService;
import com.backend.blooming.friend.application.exception.NotFoundFriendRequestException;
import com.backend.blooming.friend.infrastructure.repository.FriendRepository;
import com.backend.blooming.goal.application.GoalService;
import com.backend.blooming.goal.application.dto.CreateGoalDto;
import com.backend.blooming.themecolor.domain.ThemeColor;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Profile("test | local | dev")
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminPageService adminPageService;
    private final UserRepository userRepository;
    private final FriendService friendService;
    private final FriendRepository friendRepository;
    private final GoalService goalService;

    @GetMapping
    public String adminPage(final Model model) {
        model.addAttribute("themes", getThemes());
        model.addAttribute("users", getUsers());

        return "/admin/test";
    }

    private static List<String> getThemes() {
        return Arrays.stream(ThemeColor.values())
                     .map(ThemeColor::name)
                     .toList();
    }

    private List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest request) {
        adminPageService.createUser(request);

        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping("/friend")
    public ResponseEntity<Void> requestFriend(@RequestBody RequestFriendRequest request) {
        friendService.request(request.requestUser(), request.requestedUser());

        return ResponseEntity.noContent()
                             .build();
    }

    @PatchMapping("/friend")
    public ResponseEntity<Void> updateFriendStatus(@RequestBody UpdateFriendRequest request) {
        final Long friendId = getFriendId(request);
        updateByStatus(request, friendId);

        return ResponseEntity.noContent()
                             .build();
    }

    private Long getFriendId(final UpdateFriendRequest request) {
        final Long friendId = friendRepository.findByRequestUserIdAndRequestedUserId(
                request.requestUser(),
                request.requestedUser()
        );

        if (friendId == null) {
            throw new NotFoundFriendRequestException();
        }

        return friendId;
    }

    private void updateByStatus(final UpdateFriendRequest request, final Long friendId) {
        final FriendStatus friendStatus = FriendStatus.valueOf(request.status());

        if (FriendStatus.FRIEND.equals(friendStatus)) {
            friendService.accept(request.requestedUser(), friendId);
            return;
        }
        if (FriendStatus.DELETE_BY_REQUEST.equals(friendStatus)) {
            friendService.delete(request.requestUser(), friendId);
            return;
        }
        if (FriendStatus.DELETE_BY_REQUESTED.equals(friendStatus)) {
            friendService.delete(request.requestedUser(), friendId);
            return;
        }

        throw new InvalidFriendStatusException();
    }

    @PostMapping("/goal")
    public ResponseEntity<Void> createGoal(@RequestBody @Valid CreateGoalRequest request) {
        goalService.createGoal(convertToDto(request));

        return ResponseEntity.noContent()
                             .build();
    }

    private CreateGoalDto convertToDto(final CreateGoalRequest request) {
        return new CreateGoalDto(
                request.name(),
                request.memo(),
                request.startDate(),
                request.endDate(),
                request.manager(),
                List.of(request.manager(), request.team())
        );
    }
}
