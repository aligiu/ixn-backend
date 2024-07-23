package com.hng.ixn.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import java.nio.file.Paths;
import java.nio.file.Path;
import software.amazon.awssdk.core.ResponseInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName; // Injected from S3Config

    public String uploadFile(String key, String filePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, Paths.get(filePath));

        return putObjectResponse.eTag();
    }

    public File downloadFile(String key, String downloadPath) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);

        Path path = Paths.get(downloadPath);
        File file = new File(downloadPath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] read_buf = new byte[1024];
            int read_len;
            while ((read_len = responseInputStream.read(read_buf)) > 0) {
                fileOutputStream.write(read_buf, 0, read_len);
            }
        }

        return file;
    }

    public List<FileDetails> listFiles() {

        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);

        return response.contents().stream()
                .map(s3Object -> new FileDetails(
                        s3Object.key(),
                        generatePresignedUrl(s3Object.key())
                ))
                .collect(Collectors.toList());
    }

    private String generatePresignedUrl(String key) {
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toString();
    }

    @Value
    @AllArgsConstructor
    public static class FileDetails {
        private final String fileName;
        private final String downloadUrl;
    }

}