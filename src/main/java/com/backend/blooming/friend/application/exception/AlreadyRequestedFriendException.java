package com.backend.blooming.friend.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class AlreadyRequestedFriendException extends BloomingException {

    public AlreadyRequestedFriendException() {
        super(ExceptionMessage.ALREADY_REQUESTED_FRIEND);
    }
}
