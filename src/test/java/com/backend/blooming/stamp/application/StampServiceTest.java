package com.backend.blooming.stamp.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.stamp.application.dto.ReadStampDto;
import com.backend.blooming.stamp.application.exception.CreateStampForbiddenException;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StampServiceTest extends StampServiceTestFixture {

    @Autowired
    private StampService stampService;

    @Test
    void 새로운_스탬프를_생성한다() {
        // when
        final ReadStampDto result = stampService.createStamp(유효한_스탬프_dto);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.id()).isPositive();
            softAssertions.assertThat(result.userName()).isEqualTo(스탬프를_생성할_사용자.getName());
            softAssertions.assertThat(result.userColor()).isEqualTo(스탬프를_생성할_사용자.getColor());
            softAssertions.assertThat(result.day()).isEqualTo(유효한_스탬프_dto.day());
            softAssertions.assertThat(result.message()).isEqualTo(유효한_스탬프_dto.message());
        });
    }

    @Test
    void 존재하지_않는_사용자가_스탬프를_생성할_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampService.createStamp(존재하지_않는_사용자가_생성한_스탬프_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 스탬프를_생성하려는_골이_존재하지_않을_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampService.createStamp(존재하지_않는_골에서_생성된_스탬프_dto))
                .isInstanceOf(NotFoundGoalException.class);
    }

    @Test
    void 스탬프를_생성하려는_사용자가_골의_참여자가_아닌_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampService.createStamp(골_참여자가_아닌_사용자가_생성한_스탬프_dto))
                .isInstanceOf(CreateStampForbiddenException.class);
    }

    @Test
    void 스탬프를_생성하려는_날짜의_스탬프가_이미_존재할_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampService.createStamp(이미_존재하는_스탬프_dto))
                .isInstanceOf(InvalidStampException.InvalidStampToCreate.class);
    }
}
