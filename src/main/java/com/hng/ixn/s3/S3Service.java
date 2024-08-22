package com.hng.ixn.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.nio.file.Path;
import software.amazon.awssdk.core.ResponseInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.List;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

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

    public void deleteFile(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);

        // Optionally, you can check the deleteObjectResponse for any response details
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

    public List<FileDetails> listFiles(String folderId) {
        return listFiles().stream().filter(file -> file.getFolderId().equals(folderId)).collect(Collectors.toList());
    }

    public List<FileDetails> listFiles() {
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);

        // Assuming folderId is embedded in the key and is always the first part
        return response.contents().stream()
                .map(s3Object -> {
                    String key = s3Object.key();
                    String[] parts = key.split("/", 2);
                    String folderId = parts[0];
                    String fileName = parts.length > 1 ? parts[1] : key;
                    return new FileDetails(folderId, fileName, generateDownloadUrl(folderId, fileName));
                })
                .collect(Collectors.toList());
    }

    private String generateDownloadUrl(String folderId, String fileName) {
        // Replace with actual path to download the file
        return "/files/download/" + folderId + "?fileName=" + fileName;
    }


}