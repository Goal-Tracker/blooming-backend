package com.backend.blooming.authentication.infrastructure.blacklist;

import com.backend.blooming.authentication.domain.BlackListToken;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class BlackListTokenRepositoryTestFixture {

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    protected BlackListToken 블랙_리스트_토큰;

    @BeforeEach
    void setUpFixture() {
        블랙_리스트_토큰 = new BlackListToken("black list token", LocalDateTime.now());
        blackListTokenRepository.save(블랙_리스트_토큰);
    }
}
