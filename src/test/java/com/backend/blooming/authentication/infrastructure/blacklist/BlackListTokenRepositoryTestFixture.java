package com.backend.blooming.authentication.infrastructure.blacklist;

import com.backend.blooming.authentication.domain.BlackListToken;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class BlackListTokenRepositoryTestFixture {

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    protected BlackListToken 블랙_리스트_토큰;
    protected String 존재하지_않는_토큰 = "not exist token";

    @BeforeEach
    void setUpFixture() {
        블랙_리스트_토큰 = new BlackListToken("black list token");
        blackListTokenRepository.save(블랙_리스트_토큰);
    }
}
