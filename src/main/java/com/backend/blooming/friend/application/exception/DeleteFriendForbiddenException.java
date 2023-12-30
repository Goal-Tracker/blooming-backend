package com.backend.blooming.friend.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class DeleteFriendForbiddenException extends BloomingException {

    public DeleteFriendForbiddenException() {
        super(ExceptionMessage.DELETE_FRIEND_FORBIDDEN);
    }
}
