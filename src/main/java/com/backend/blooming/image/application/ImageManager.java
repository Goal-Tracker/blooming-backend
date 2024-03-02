package com.backend.blooming.image.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageManager {

    String upload(final MultipartFile multipartFile, final String path);
}
