package com.backend.blooming.admin.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminPageServiceTest extends AdminPageServiceTestFixture {

    @Autowired
    private AdminPageService adminPageService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 사용자를_저장한다() {
        // when
        final Long actual = adminPageService.createUser(사용자_생성_요청);

        // then
        final User user = userRepository.findById(actual).get();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(user.getName()).isEqualTo(사용자_생성_요청.name());
            softAssertions.assertThat(user.getEmail()).isEqualTo(사용자_생성_요청.email());
            softAssertions.assertThat(user.getColor().name()).isEqualTo(사용자_생성_요청.theme());
            softAssertions.assertThat(user.getStatusMessage()).isEqualTo(사용자_생성_요청.statusMessage());
        });
    }

    @Test
    void 사용자_생성시_이메일이_없다면_기본_이메일로_저장한다() {
        // when
        final Long actual = adminPageService.createUser(이메일_없이_사용자_생성_요청);

        // then
        final User user = userRepository.findById(actual).get();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(user.getName()).isEqualTo(사용자_생성_요청.name());
            softAssertions.assertThat(user.getEmail()).isEqualTo("test@email.com");
            softAssertions.assertThat(user.getColor().name()).isEqualTo(사용자_생성_요청.theme());
            softAssertions.assertThat(user.getStatusMessage()).isEqualTo(사용자_생성_요청.statusMessage());
        });
    }
}
