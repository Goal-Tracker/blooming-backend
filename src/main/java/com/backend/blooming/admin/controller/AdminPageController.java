package com.backend.blooming.admin.controller;

import com.backend.blooming.admin.application.AdminPageService;
import com.backend.blooming.admin.controller.dto.CreateUserRequest;
import com.backend.blooming.admin.controller.dto.RequestFriendRequest;
import com.backend.blooming.friend.application.FriendService;
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

    @PostMapping("/friend/request")
    public ResponseEntity<Void> requestFriend(@RequestBody RequestFriendRequest request) {
        friendService.request(request.requestUser(), request.requestedUser());

        return ResponseEntity.noContent()
                             .build();
    }
}
