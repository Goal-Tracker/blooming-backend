package com.backend.blooming.image.infrastructure.s3;

import com.backend.blooming.image.application.util.ImageStoragePath;
import com.backend.blooming.image.infrastructure.exception.UploadImageException;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.shaded.com.google.common.net.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageStorageS3ManagerTest {

    @Autowired
    private ImageStorageS3Manager imageStorageS3Manager;

    @Value("${cloud.aws.s3.path}")
    private String basicPath;

    @Value("${cloud.aws.cloud-front.domain}")
    private String domain;

    @Test
    void 파일을_S3_서버에_저장한다() {
        // given
        final String fileName = "image.png";
        final MediaType mediaType = MediaType.PNG;
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                fileName,
                mediaType.toString(),
                "image".getBytes()
        );
        final ImageStoragePath path = ImageStoragePath.PROFILE;

        // when
        final String actual = imageStorageS3Manager.upload(image, path);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).contains(domain, basicPath, path.getPath(), mediaType.subtype());
            softAssertions.assertThat(actual).doesNotContain(fileName);
        });
    }

    @Test
    void 파일_저장시_파일이_비어있다면_예외가_발생한다() {
        // given
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.PNG.toString(),
                new byte[0]
        );
        final ImageStoragePath path = ImageStoragePath.PROFILE;

        // when & then
        Assertions.assertThatThrownBy(() -> imageStorageS3Manager.upload(image, path))
                  .isInstanceOf(UploadImageException.EmptyFileException.class);
    }

    @Test
    void 파일_저장시_파일_저장_경로가_비어있다면_예외가_발생한다() {
        // given
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.PNG.toString(),
                "image".getBytes()
        );
        final ImageStoragePath path = null;

        // when & then
        Assertions.assertThatThrownBy(() -> imageStorageS3Manager.upload(image, path))
                  .isInstanceOf(UploadImageException.EmptyPathException.class);
    }

    @Test
    void 파일_저장시_지원하지_않는_확장자라면_예외가_발생한다() {
        // given
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.bmp",
                MediaType.BMP.toString(),
                "image".getBytes()
        );
        final ImageStoragePath path = ImageStoragePath.PROFILE;

        // when & then
        Assertions.assertThatThrownBy(() -> imageStorageS3Manager.upload(image, path))
                  .isInstanceOf(UploadImageException.NotSupportedMediaTypeException.class);
    }
}
