package com.backend.blooming.authentication.application;

import com.backend.blooming.authentication.domain.BlackListToken;
import com.backend.blooming.authentication.infrastructure.blacklist.BlackListTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class BlackListTokenServiceTestFixture {

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    protected String 토큰 = "refresh token";
    protected String 이미_등록된_토큰 = "already register refresh token";

    @BeforeEach
    void setUpFixture() {
        final BlackListToken 블랙_리스트_토큰 = new BlackListToken(이미_등록된_토큰);
        blackListTokenRepository.save(블랙_리스트_토큰);
    }
}
