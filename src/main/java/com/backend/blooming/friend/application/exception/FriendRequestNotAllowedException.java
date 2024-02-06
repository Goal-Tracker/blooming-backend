package com.backend.blooming.friend.application.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class FriendRequestNotAllowedException extends BloomingException {


    private FriendRequestNotAllowedException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class SelfRequestNotAllowedException extends FriendRequestNotAllowedException {

        public SelfRequestNotAllowedException() {
            super(ExceptionMessage.SELF_REQUEST_NOT_ALLOWED);
        }
    }

    public static class AlreadyRequestedFriendException extends FriendRequestNotAllowedException {

        public AlreadyRequestedFriendException() {
            super(ExceptionMessage.ALREADY_REQUESTED_FRIEND);
        }
    }
}
