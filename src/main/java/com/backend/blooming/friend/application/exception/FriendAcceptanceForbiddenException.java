package com.backend.blooming.friend.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class FriendAcceptanceForbiddenException extends BloomingException {

    public FriendAcceptanceForbiddenException() {
        super(ExceptionMessage.FRIEND_ACCEPTANCE_FORBIDDEN);
    }
}
