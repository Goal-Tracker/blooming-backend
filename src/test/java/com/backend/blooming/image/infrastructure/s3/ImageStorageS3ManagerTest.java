package com.backend.blooming.image.infrastructure.s3;

import com.backend.blooming.image.infrastructure.exception.UploadImageException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.willThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageStorageS3ManagerTest extends ImageStorageS3ManagerTestFixture {

    @Autowired
    private ImageStorageS3Manager imageStorageS3Manager;

    @SpyBean
    private S3Client s3Client;

    @Value("${cloud.aws.s3.path}")
    private String basicPath;

    @Value("${cloud.aws.cloud-front.domain}")
    private String domain;

    @Test
    void 파일을_S3_서버에_저장한다() {
        // when
        final String actual = imageStorageS3Manager.upload(이미지_파일, 저장_경로);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).contains(domain, basicPath, 저장_경로.getPath(), 파일_타입.subtype());
            softAssertions.assertThat(actual).doesNotContain(파일명);
        });
    }

    @Test
    void 파일_저장시_파일이_비어있다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> imageStorageS3Manager.upload(빈_이미지_파일, 저장_경로))
                .isInstanceOf(UploadImageException.EmptyFileException.class);
    }

    @Test
    void 파일_저장시_파일_저장_경로가_비어있다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> imageStorageS3Manager.upload(이미지_파일, null))
                .isInstanceOf(UploadImageException.EmptyPathException.class);
    }

    @Test
    void 파일_저장시_지원하지_않는_확장자라면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> imageStorageS3Manager.upload(지원하지_않는_확장자의_파일, 저장_경로))
                .isInstanceOf(UploadImageException.NotSupportedMediaTypeException.class);
    }

    @Test
    void 파일_저장시_s3에_저장시_문제가_발생한_경우_예외가_발생한다() {
        // given
        willThrow(SdkException.class).given(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        // when & then
        assertThatThrownBy(() -> imageStorageS3Manager.upload(이미지_파일, 저장_경로))
                .isInstanceOf(UploadImageException.SdkException.class);
    }
}
