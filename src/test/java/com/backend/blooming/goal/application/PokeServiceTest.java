package com.backend.blooming.goal.application;

import com.backend.blooming.configuration.IsolateDatabase;
import com.backend.blooming.goal.application.exception.ForbiddenGoalToPokeException;
import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.notification.domain.Notification;
import com.backend.blooming.notification.domain.NotificationType;
import com.backend.blooming.notification.infrastructure.repository.NotificationRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PokeServiceTest extends PokeServiceTestFixture {

    @Autowired
    private PokeService pokeService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 특정_골의_사용자에게_콕_찌를_수_있다() {
        // when
        pokeService.poke(콕_찌르기_요청_dto);

        // then
        final List<Notification> notifications = notificationRepository.findAllByReceiverId(콕_찌르기를_받은_사용자_아이디);
        final Notification pokeNotification = notifications.get(0);
        final User user = userRepository.findById(콕_찌르기를_받은_사용자_아이디).get();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(notifications).isNotEmpty();
            softAssertions.assertThat(pokeNotification.getRequestId()).isEqualTo(콕_찌르기를_요청한_사용자_아이디);
            softAssertions.assertThat(pokeNotification.getReceiver().getId()).isEqualTo(콕_찌르기를_받은_사용자_아이디);
            softAssertions.assertThat(pokeNotification.getType()).isEqualTo(NotificationType.POKE);
            softAssertions.assertThat(user.isNewAlarm()).isTrue();
        });
    }

    @Test
    void 특정_골의_사용자에게_콕찌를_때_존재하지_않는_골이라면_예외를_반환한다() {
        assertThatThrownBy(() -> pokeService.poke(존재하지_않는_골에_대한_콕_찌르기_요청_dto))
                .isInstanceOf(NotFoundGoalException.class);
    }

    @Test
    void 특정_골의_사용자에게_콕찌를_때_요청자가_존재하지_않는다면_예외를_반환한다() {
        assertThatThrownBy(() -> pokeService.poke(존재하지_않는_사용자가_콕_찌르기_요청_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 특정_골의_사용자에게_콕찌를_때_존재하지_않는_사용자에게_요청하면_예외를_반환한다() {
        assertThatThrownBy(() -> pokeService.poke(존재하지_않는_사용자에게_콕_찌르기_요청_dto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 특정_골의_포함되어_있지_않는_사용자가_콕찌르는_경우_예외를_반환한다() {
        assertThatThrownBy(() -> pokeService.poke(골에_포함되어_있지_않는_사용자가_콕_찌르기_요청_dto))
                .isInstanceOf(ForbiddenGoalToPokeException.SenderNotInGoalTeam.class);
    }

    @Test
    void 특정_골의_포함되어_있지_않는_사용자에게_콕찌르는_경우_예외를_반환한다() {
        assertThatThrownBy(() -> pokeService.poke(골에_포함되어_있지_않는_사용자에게_콕_찌르기_요청_dto))
                .isInstanceOf(ForbiddenGoalToPokeException.ReceiverNotInGoalTeam.class);
    }
}
