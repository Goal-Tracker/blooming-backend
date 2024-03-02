package com.backend.blooming.image.infrastructure.exception;

import com.backend.blooming.exception.BloomingException;
import com.backend.blooming.exception.ExceptionMessage;

public class UploadImageException extends BloomingException {

    private UploadImageException(final ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public static class EmptyFileException extends UploadImageException {

        public EmptyFileException() {
            super(ExceptionMessage.EMPTY_IMAGE_FILE);
        }
    }

    public static class EmptyPathException extends UploadImageException {

        public EmptyPathException() {
            super(ExceptionMessage.EMPTY_IMAGE_PATH);
        }
    }

    public static class FileControlException extends UploadImageException {

        public FileControlException() {
            super(ExceptionMessage.UPLOAD_FILE_CONTROL);
        }
    }

    public static class SdkException extends UploadImageException {

        public SdkException() {
            super(ExceptionMessage.UPLOAD_SDK);
        }
    }

    public static class NotSupportedMediaTypeException extends UploadImageException {

        public NotSupportedMediaTypeException() {
            super(ExceptionMessage.NOT_SUPPORTED_MEDIA_TYPE);
        }
    }
}
