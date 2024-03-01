package com.backend.blooming.image.infrastructure.s3;

import com.backend.blooming.image.application.ImageManager;
import com.backend.blooming.image.infrastructure.exception.UploadImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageS3Manager implements ImageManager {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.path}")
    private String path;

    @Override
    public String upload(final MultipartFile multipartFile) {
        try {
            final String imagePath = path + multipartFile.getOriginalFilename();
            final PutObjectRequest request = getPutObjectRequest(imagePath);
            final RequestBody requestBody = getRequestBody(multipartFile);
            s3Client.putObject(request, requestBody);

            return imagePath;
        } catch (final IOException exception) {
            throw new UploadImageException.FileControlException();
        } catch (final SdkException exception) {
            throw new UploadImageException.SdkException();
        }
    }

    private PutObjectRequest getPutObjectRequest(final String filename) {
        return PutObjectRequest.builder()
                               .bucket(bucket)
                               .key(filename)
                               .build();
    }

    private static RequestBody getRequestBody(final MultipartFile multipartFile) throws IOException {
        return RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
    }
}
