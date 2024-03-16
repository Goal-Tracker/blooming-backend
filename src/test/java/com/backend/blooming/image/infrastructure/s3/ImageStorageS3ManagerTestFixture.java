package com.backend.blooming.image.infrastructure.s3;

import com.backend.blooming.image.application.util.ImageStoragePath;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.shaded.com.google.common.net.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class ImageStorageS3ManagerTestFixture {

    protected String 파일명 = "image.png";
    protected MediaType 파일_타입 = MediaType.PNG;
    protected ImageStoragePath 저장_경로 = ImageStoragePath.PROFILE;
    protected MockMultipartFile 이미지_파일 = new MockMultipartFile(
            "image",
            파일명,
            파일_타입.toString(),
            "image".getBytes()
    );
    protected MockMultipartFile 빈_이미지_파일 = new MockMultipartFile(
            "image",
            "image.png",
            MediaType.PNG.toString(),
            new byte[0]
    );
    protected MockMultipartFile 지원하지_않는_확장자의_파일 = new MockMultipartFile(
            "image",
            "image.bmp",
            MediaType.BMP.toString(),
            "image".getBytes()
    );
}
