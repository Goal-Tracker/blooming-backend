package com.backend.blooming.image.application;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageManager {

    String upload(final MultipartFile multipartFile) throws IOException;
}
