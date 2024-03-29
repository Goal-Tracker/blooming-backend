package com.backend.blooming.stamp.application;

import com.backend.blooming.goal.application.exception.NotFoundGoalException;
import com.backend.blooming.goal.domain.Goal;
import com.backend.blooming.goal.infrastructure.repository.GoalRepository;
import com.backend.blooming.image.application.ImageStorageManager;
import com.backend.blooming.image.application.util.ImageStoragePath;
import com.backend.blooming.stamp.application.dto.CreateStampDto;
import com.backend.blooming.stamp.application.dto.ReadAllStampDto;
import com.backend.blooming.stamp.application.dto.ReadStampDto;
import com.backend.blooming.stamp.application.exception.ForbiddenStampToCreateException;
import com.backend.blooming.stamp.application.exception.ForbiddenStampToReadException;
import com.backend.blooming.stamp.domain.Day;
import com.backend.blooming.stamp.domain.Message;
import com.backend.blooming.stamp.domain.Stamp;
import com.backend.blooming.stamp.domain.exception.InvalidStampException;
import com.backend.blooming.stamp.infrastructure.repository.StampRepository;
import com.backend.blooming.user.application.exception.NotFoundUserException;
import com.backend.blooming.user.domain.User;
import com.backend.blooming.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StampService {

    private static final String DEFAULT_STAMP_IMAGE_URL = "";

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final StampRepository stampRepository;
    private final ImageStorageManager imageStorageManager;

    public ReadStampDto createStamp(final CreateStampDto createStampDto) {
        final Goal goal = getGoal(createStampDto.goalId());
        final User user = getUser(createStampDto.userId());
        validateUserToCreateStamp(goal, user);
        validateExistStamp(user.getId(), createStampDto.day());
        final String stampImageUrl = saveStampImageUrl(createStampDto.stampImage());
        final Stamp stamp = persistStamp(createStampDto, goal, user, stampImageUrl);

        return ReadStampDto.from(stamp);
    }

    private Goal getGoal(final Long goalId) {
        return goalRepository.findByIdWithUserAndDeletedIsFalse(goalId)
                             .orElseThrow(NotFoundGoalException::new);
    }

    private User getUser(final Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                             .orElseThrow(NotFoundUserException::new);
    }

    private void validateUserToCreateStamp(final Goal goal, final User user) {
        if (!goal.isTeamAndAccepted(user)) {
            throw new ForbiddenStampToCreateException();
        }
    }

    private void validateExistStamp(final Long userId, final long day) {
        final boolean isExistsStamp = stampRepository.existsByUserIdAndDayAndDeletedIsFalse(userId, day);
        if (isExistsStamp) {
            throw new InvalidStampException.InvalidStampToCreate();
        }
    }

    private String saveStampImageUrl(final MultipartFile stampImage) {
        if (stampImage != null && !stampImage.isEmpty()) {
            return imageStorageManager.upload(stampImage, ImageStoragePath.STAMP);
        }

        return DEFAULT_STAMP_IMAGE_URL;
    }

    private Stamp persistStamp(
            final CreateStampDto createStampDto,
            final Goal goal,
            final User user,
            final String stampImageUrl
    ) {
        final Stamp stamp = Stamp.builder()
                                 .goal(goal)
                                 .user(user)
                                 .day(new Day(goal.getGoalTerm(), createStampDto.day()))
                                 .message(new Message(createStampDto.message()))
                                 .stampImageUrl(stampImageUrl)
                                 .build();

        return stampRepository.save(stamp);
    }

    @Transactional(readOnly = true)
    public ReadAllStampDto readAllByGoalId(final Long goalId, final Long userId) {
        final Goal goal = getGoal(goalId);
        final User user = getUser(userId);
        validateUserToRead(goal, user);

        final List<Stamp> stamps = stampRepository.findAllByGoalIdAndDeletedIsFalse(goal.getId());

        return ReadAllStampDto.from(stamps);
    }

    private void validateUserToRead(final Goal goal, final User user) {
        if (!goal.isTeamAndAccepted(user)) {
            throw new ForbiddenStampToReadException();
        }
    }
}
