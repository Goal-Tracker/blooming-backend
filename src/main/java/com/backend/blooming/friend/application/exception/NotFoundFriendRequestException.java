package com.backend.blooming.friend.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class NotFoundFriendRequestException extends BloomingException {

    public NotFoundFriendRequestException() {
        super(ExceptionMessage.NOT_FOUND_FRIEND_REQUEST);
    }
}
