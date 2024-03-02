package com.backend.blooming.image.application;

import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.google.common.net.MediaType;

import java.util.List;

public interface ImageManager {

    List<MediaType> SUPPORTED_MEDIA_TYPE = List.of(MediaType.PNG, MediaType.JPEG, MediaType.parse("image/jpg"));
    String EXTENSION_DOT = ".";

    String upload(final MultipartFile multipartFile, final String path);
}
