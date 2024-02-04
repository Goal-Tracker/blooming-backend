package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.application.exception.AlreadyRegisterBlackListTokenException;
import com.backend.blooming.authentication.domain.BlackListToken;
import com.backend.blooming.authentication.infrastructure.blacklist.BlackListTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BlackListTokenService {

    private final BlackListTokenRepository blackListTokenRepository;

    public Long register(final String token) {
        validateToken(token);
        final BlackListToken blackListToken = new BlackListToken(token);

        return blackListTokenRepository.save(blackListToken)
                                       .getId();
    }

    private void validateToken(String token) {
        if (blackListTokenRepository.existsByToken(token)) {
            throw new AlreadyRegisterBlackListTokenException();
        }
    }
}
