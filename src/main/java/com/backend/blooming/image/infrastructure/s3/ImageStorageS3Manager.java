package com.backend.blooming.image.infrastructure.s3;

import com.backend.blooming.image.application.ImageStorageManager;
import com.backend.blooming.image.application.util.ImageStoragePath;
import com.backend.blooming.image.infrastructure.exception.UploadImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.google.common.net.MediaType;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageStorageS3Manager implements ImageStorageManager {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.path}")
    private String basicPath;

    @Value("${cloud.aws.cloud-front.domain}")
    private String domain;

    @Override
    public String upload(final MultipartFile multipartFile, final ImageStoragePath path) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new UploadImageException.EmptyFileException();
        }
        if (path == null) {
            throw new UploadImageException.EmptyPathException();
        }

        return uploadImage(multipartFile, path);
    }

    private String uploadImage(final MultipartFile multipartFile, final ImageStoragePath path) {
        try {
            final String imagePath = getImagePath(path, multipartFile);
            final PutObjectRequest request = getPutObjectRequest(imagePath);
            final RequestBody requestBody = getRequestBody(multipartFile);
            s3Client.putObject(request, requestBody);

            return domain + imagePath;
        } catch (final IOException exception) {
            throw new UploadImageException.FileControlException();
        } catch (final SdkException exception) {
            throw new UploadImageException.SdkException();
        }
    }

    private String getImagePath(final ImageStoragePath path, final MultipartFile multipartFile) {
        return basicPath + path.getPath() + UUID.randomUUID() + getExtension(multipartFile.getContentType());
    }

    private String getExtension(final String contentType) {
        final MediaType mediaType = MediaType.parse(contentType);
        if (!SUPPORTED_MEDIA_TYPE.contains(mediaType)) {
            throw new UploadImageException.NotSupportedMediaTypeException();
        }

        return EXTENSION_DOT + mediaType.subtype();
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
