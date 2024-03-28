package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.application.dto.LoginDto;
import com.backend.blooming.authentication.application.dto.LoginInformationDto;
import com.backend.blooming.authentication.application.dto.LoginUserInformationDto;
import com.backend.blooming.authentication.application.dto.LogoutDto;
import com.backend.blooming.authentication.application.dto.TokenDto;
import com.backend.blooming.authentication.application.util.OAuthClientComposite;
import com.backend.blooming.authentication.infrastructure.exception.InvalidTokenException;
import com.backend.blooming.authentication.infrastructure.jwt.TokenProvider;
import com.backend.blooming.authentication.infrastructure.jwt.TokenType;
import com.backend.blooming.authentication.infrastructure.jwt.dto.AuthClaims;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthClient;
import com.backend.blooming.authentication.infrastructure.oauth.OAuthType;
import com.backend.blooming.authentication.infrastructure.oauth.dto.UserInformationDto;
import com.backend.blooming.devicetoken.application.service.DeviceTokenService;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.Email;
import com.backend.blooming.user.domain.Name;
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

    private static final int NAME_MAX_LENGTH = 50;
    private static final int BEGIN_INDEX = 0;
    private final OAuthClientComposite oAuthClientComposite;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final BlackListTokenService blackListTokenService;
    private final DeviceTokenService deviceTokenService;

    public LoginInformationDto login(final OAuthType oAuthType, final LoginDto loginDto) {
        final OAuthClient oAuthClient = oAuthClientComposite.findOAuthClient(oAuthType);
        final UserInformationDto userInformationDto = oAuthClient.findUserInformation(loginDto.oAuthAccessToken());
        final LoginUserInformationDto userInformation = findOrPersistUserInformation(userInformationDto, oAuthType);
        saveOrActiveToken(userInformation.user(), loginDto.deviceToken());

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
                                   .name(convertToNameAndTruncateLength(userInformationDto.oAuthId()))
                                   .email(new Email(userInformationDto.email()))
                                   .build();

        return userRepository.save(savedUser);
    }

    private Name convertToNameAndTruncateLength(final String oAuthId) {
        if (oAuthId.length() > NAME_MAX_LENGTH) {
            return new Name(oAuthId.substring(BEGIN_INDEX, NAME_MAX_LENGTH));
        }

        return new Name(oAuthId);
    }

    private TokenDto convertToTokenDto(final User user) {
        final Long userId = user.getId();
        final String accessToken = tokenProvider.createToken(TokenType.ACCESS, userId);
        final String refreshToken = tokenProvider.createToken(TokenType.REFRESH, userId);

        return new TokenDto(accessToken, refreshToken);
    }

    private void saveOrActiveToken(final User user, final String deviceToken) {
        if (deviceToken != null && !deviceToken.isBlank()) {
            deviceTokenService.saveOrActivate(user.getId(), deviceToken);
        }
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
            throw new InvalidTokenException();
        }
    }

    public void logout(final Long userId, final LogoutDto logoutDto) {
        final AuthClaims authClaims = tokenProvider.parseToken(TokenType.REFRESH, logoutDto.refreshToken());
        final User user = validateAndGetUser(userId, authClaims);

        blackListTokenService.register(logoutDto.refreshToken());
        deviceTokenService.deactivate(user.getId(), logoutDto.deviceToken());
    }

    private User validateAndGetUser(final Long userId, final AuthClaims authClaims) {
        if (!userId.equals(authClaims.userId())) {
            throw new InvalidTokenException();
        }

        return userRepository.findById(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    public void withdraw(final Long userId, final String refreshToken) {
        final AuthClaims authClaims = tokenProvider.parseToken(TokenType.REFRESH, refreshToken);
        final User user = validateAndGetUser(userId, authClaims);
        final OAuthClient oAuthClient = oAuthClientComposite.findOAuthClient(user.getOAuthType());

        user.delete();
        blackListTokenService.register(refreshToken);
        deviceTokenService.deactivateAllByUserId(user.getId());
        oAuthClient.unlink(user.getOAuthId())
                   .subscribe();
    }
}
