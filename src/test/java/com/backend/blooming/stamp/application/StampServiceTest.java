package com.backend.blooming.stamp.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.application.dto.ReadAllStampDto;
import com.backend.blooming.stamp.application.dto.ReadStampDto;
import com.backend.blooming.stamp.application.exception.ForbiddenStampToCreateException;
import com.backend.blooming.stamp.application.exception.ForbiddenStampToReadException;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StampServiceTest extends StampServiceTestFixture {

    @Autowired
    private StampService stampService;

    @Autowired
    private GoalRepository goalRepository;

    @Test
    void 새로운_스탬프를_생성한다() {
        // given
        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(19))
                           .managerId(스탬프를_생성할_사용자.getId())
                           .users(List.of(스탬프를_생성할_사용자))
                           .build();
        goalRepository.save(골);

        final CreateStampDto 유효한_스탬프_dto = new CreateStampDto(
                골.getId(),
                스탬프를_생성할_사용자.getId(),
                1,
                "스탬프 메시지",
                추가할_스탬프_이미지
        );

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
    void 스탬프_이미지가_null이거나_빈값인_경우_빈_문자열을_주소로_저장한다() {
        // given
        final Goal 골 = Goal.builder()
                           .name("골 제목")
                           .memo("골 메모")
                           .startDate(LocalDate.now())
                           .endDate(LocalDate.now().plusDays(19))
                           .managerId(스탬프를_생성할_사용자.getId())
                           .users(List.of(스탬프를_생성할_사용자))
                           .build();
        goalRepository.save(골);

        final CreateStampDto 스탬프_이미지가_null인_스탬프_dto = new CreateStampDto(
                골.getId(),
                스탬프를_생성할_사용자.getId(),
                1,
                "스탬프 메시지",
                null
        );

        // when
        final ReadStampDto result = stampService.createStamp(스탬프_이미지가_null인_스탬프_dto);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.id()).isPositive();
            softAssertions.assertThat(result.userName()).isEqualTo(스탬프를_생성할_사용자.getName());
            softAssertions.assertThat(result.userColor()).isEqualTo(스탬프를_생성할_사용자.getColor());
            softAssertions.assertThat(result.day()).isEqualTo(스탬프_이미지가_null인_스탬프_dto.day());
            softAssertions.assertThat(result.message()).isEqualTo(스탬프_이미지가_null인_스탬프_dto.message());
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
    void 스탬프를_생성하려는_사용자가_골의_참여자가_아니거나_골_초대를_수락하지_않은_경우_예외를_발생한다() {
        // when & then
        assertSoftly(softAssertions -> {
            assertThatThrownBy(() -> stampService.createStamp(골_참여자가_아닌_사용자가_생성한_스탬프_dto))
                    .isInstanceOf(ForbiddenStampToCreateException.class);
            assertThatThrownBy(() -> stampService.createStamp(골_초대를_수락하지_않은_사용자가_생성한_스탬프_dto))
                    .isInstanceOf(ForbiddenStampToCreateException.class);
        });
    }

    @Test
    void 스탬프를_생성하려는_날짜의_스탬프가_이미_존재할_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampService.createStamp(이미_존재하는_스탬프_dto))
                .isInstanceOf(InvalidStampException.InvalidStampToCreate.class);
    }

    @Test
    void 요청한_골_아이디에_대한_모든_스탬프를_조회한다() {
        // when
        final ReadAllStampDto result = stampService.readAllByGoalId(유효한_골_아이디, 스탬프를_생성한_사용자_아이디1);

        // then
        assertSoftly(softAssertions -> {
            final List<ReadAllStampDto.StampDto> stamps = result.stamps();
            softAssertions.assertThat(stamps).hasSize(2);
            softAssertions.assertThat(stamps.get(0).userId()).isEqualTo(스탬프를_생성한_사용자_아이디1);
            softAssertions.assertThat(stamps.get(1).userId()).isEqualTo(스탬프를_생성한_사용자_아이디2);
            softAssertions.assertThat(stamps.get(0).name()).isEqualTo(스탬프를_생성한_사용자_이름1);
            softAssertions.assertThat(stamps.get(1).name()).isEqualTo(스탬프를_생성한_사용자_이름2);
            softAssertions.assertThat(stamps.get(0).stampImageUrl()).isEqualTo(스탬프_이미지_url);
            softAssertions.assertThat(stamps.get(1).stampImageUrl()).isEqualTo(비어있는_스탬프_이미지);
        });
    }

    @Test
    void 스탬프_조회시_존재하지_않는_골의_스탬프를_조회할_경우_예외를_발생한다() {
        // when & then
        assertThatThrownBy(() -> stampService.readAllByGoalId(존재하지_않는_골_아이디, 스탬프를_생성한_사용자_아이디1))
                .isInstanceOf(NotFoundGoalException.class);
    }

    @Test
    void 골_참여자가_아니거나_골_초대를_수락하지_않은_사용자가_스탬프_조회를_요청한_경우_예외를_발생한다() {
        // when & then
        assertSoftly(softAssertions -> {
            assertThatThrownBy(() -> stampService.readAllByGoalId(유효한_골_아이디, 골_참여자가_아닌_사용자_아이디))
                    .isInstanceOf(ForbiddenStampToReadException.class);
            assertThatThrownBy(() -> stampService.readAllByGoalId(유효한_골_아이디, 골_초대를_수락하지_않은_사용자_아이디))
                    .isInstanceOf(ForbiddenStampToReadException.class);
        });
    }
}
