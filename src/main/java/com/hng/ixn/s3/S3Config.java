package com.hng.ixn.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        String accessKey = System.getenv("AWS_IXN_USER_ACCESS_KEY");
        String secretKey = System.getenv("AWS_IXN_USER_SECRET_KEY");

        System.out.println("Access Key: " + accessKey);
        System.out.println("Secret Key: " + secretKey);

        if (accessKey == null || secretKey == null) {
            throw new IllegalArgumentException("AWS credentials not set in environment variables");
        }

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}