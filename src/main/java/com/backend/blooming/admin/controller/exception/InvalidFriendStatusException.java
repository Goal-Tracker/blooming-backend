package com.backend.blooming.admin.controller.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class InvalidFriendStatusException extends BloomingException {

    public InvalidFriendStatusException() {
        super(ExceptionMessage.INVALID_FRIEND_STATUS);
    }
}
