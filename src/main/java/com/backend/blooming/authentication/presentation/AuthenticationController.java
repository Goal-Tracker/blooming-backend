package com.backend.blooming.authentication.presentation;

import com.backend.blooming.authentication.application.AuthenticationService;
import com.backend.blooming.authentication.application.dto.LoginInformationDto;
import com.backend.blooming.authentication.application.dto.TokenDto;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.presentation.dto.request.ReissueAccessTokenRequest;
import com.backend.blooming.authentication.presentation.dto.response.LoginInformationResponse;
import com.backend.blooming.authentication.presentation.dto.response.SocialLoginRequest;
import com.backend.blooming.authentication.presentation.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login/oauth/{oAuthType}", headers = "X-API-VERSION=1")
    public ResponseEntity<LoginInformationResponse> login(
            @PathVariable final String oAuthType,
            @RequestBody final SocialLoginRequest loginRequest
    ) {
        final LoginInformationDto loginInformationDto = authenticationService.login(
                OAuthType.from(oAuthType),
                loginRequest.accessToken()
        );

        return ResponseEntity.ok(LoginInformationResponse.from(loginInformationDto));
    }

    @PostMapping(value = "/reissue", headers = "X-API-VERSION=1")
    public ResponseEntity<TokenResponse> reissueAccessToken(
            @RequestBody final ReissueAccessTokenRequest reissueRequest
    ) {
        final TokenDto tokenDto = authenticationService.reissueAccessToken(reissueRequest.refreshToken());

        return ResponseEntity.ok(TokenResponse.from(tokenDto));
    }
}
