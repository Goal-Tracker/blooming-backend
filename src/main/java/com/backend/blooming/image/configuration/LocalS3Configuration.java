package com.backend.blooming.image.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service;

@Profile("test | local")
@Configuration
public class LocalS3Configuration {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:0.11.3");

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer(LOCALSTACK_IMAGE).withServices(Service.S3);
    }

    @Bean
    public S3Client s3Client(final LocalStackContainer localStack) {
        final S3Client s3Client = S3Client.builder()
                                          .endpointOverride(localStack.getEndpoint())
                                          .credentialsProvider(getAwsCredentialsProvider(localStack))
                                          .region(Region.of(localStack.getRegion()))
                                          .build();
        final CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                                                                     .bucket(bucket)
                                                                     .build();
        s3Client.createBucket(bucketRequest);

        return s3Client;
    }

    @NotNull
    private static StaticCredentialsProvider getAwsCredentialsProvider(final LocalStackContainer localStack) {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
        );
    }
}
