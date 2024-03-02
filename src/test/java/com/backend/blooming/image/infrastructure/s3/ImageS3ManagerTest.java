package com.backend.blooming.image.infrastructure.s3;

import com.backend.blooming.image.infrastructure.exception.UploadImageException;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.shaded.com.google.common.net.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageS3ManagerTest {

    @Autowired
    private ImageS3Manager imageS3Manager;

    @Value("${cloud.aws.s3.path}")
    private String basicPath;

    @Value("${cloud.aws.cloud-front.domain}")
    private String domain;

    @Test
    void 파일을_S3_서버에_저장한다() {
        // given
        final String fileName = "image.png";
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                fileName,
                MediaType.PNG.toString(),
                "image".getBytes()
        );
        final String path = "profile";

        // when
        final String actual = imageS3Manager.upload(image, path);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).contains(domain, basicPath, path);
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
        final String path = "profile";

        // when & then
        Assertions.assertThatThrownBy(() -> imageS3Manager.upload(image, path))
                  .isInstanceOf(UploadImageException.EmptyFileException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 파일_저장시_파일_저장_경로가_비어있다면_예외가_발생한다(final String path) {
        // given
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.PNG.toString(),
                "image".getBytes()
        );

        // when & then
        Assertions.assertThatThrownBy(() -> imageS3Manager.upload(image, path))
                  .isInstanceOf(UploadImageException.EmptyPathException.class);
    }
}
