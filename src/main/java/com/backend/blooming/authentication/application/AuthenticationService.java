package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.application.dto.LoginInformationDto;
import com.backend.blooming.authentication.application.dto.LoginUserInformationDto;
import com.backend.blooming.authentication.application.dto.TokenDto;
import com.backend.blooming.authentication.application.exception.UnauthorizedAccessTokenException;
import com.backend.blooming.authentication.application.util.OAuthClientComposite;
import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthClient;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final OAuthClientComposite oAuthClientComposite;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public LoginInformationDto login(final OAuthType oAuthType, final String oAuthAccessToken) {
        final OAuthClient oAuthClient = oAuthClientComposite.findOAuthClient(oAuthType);
        final UserInformationDto userInformationDto = oAuthClient.findUserInformation(oAuthAccessToken);
        final LoginUserInformationDto userInformation = findOrPersistUserInformation(userInformationDto, oAuthType);

        return new LoginInformationDto(convertToTokenDto(userInformation.user()), userInformation.isSignUp());
    }

    private LoginUserInformationDto findOrPersistUserInformation(
            final UserInformationDto userInformationDto,
            final OAuthType oAuthType
    ) {
        final AtomicBoolean isSignUp = new AtomicBoolean(false);
        final User user = userRepository.findByOAuthIdAndOAuthType(userInformationDto.oAuthId(), oAuthType)
                                        .orElseGet(() -> {
                                                    isSignUp.set(true);
                                                    return persistUser(userInformationDto, oAuthType);
                                                }
                                        );

        return new LoginUserInformationDto(user, isSignUp.get());
    }

    private User persistUser(final UserInformationDto userInformationDto, final OAuthType oAuthType) {
        final User savedUser = User.builder()
                                   .oAuthId(userInformationDto.oAuthId())
                                   .oAuthType(oAuthType)
                                   .email(userInformationDto.email())
                                   .build();

        return userRepository.save(savedUser);
    }

    private TokenDto convertToTokenDto(final User user) {
        final Long userId = user.getId();
        final String accessToken = tokenProvider.createToken(TokenType.ACCESS, userId);
        final String refreshToken = tokenProvider.createToken(TokenType.REFRESH, userId);

        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public TokenDto reissueAccessToken(final String refreshToken) {
        final AuthClaims authClaims = tokenProvider.parseToken(TokenType.REFRESH, refreshToken);
        validateUser(authClaims.userId());

        final String accessToken = tokenProvider.createToken(TokenType.ACCESS, authClaims.userId());

        return new TokenDto(accessToken, refreshToken);
    }

    private void validateUser(final Long userId) {
        if (!userRepository.existsByIdAndDeletedIsFalse(userId)) {
            throw new UnauthorizedAccessTokenException();
        }
    }
}
