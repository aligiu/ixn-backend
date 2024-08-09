package com.hng.ixn.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile() {
        // Arrange
        String key = "test/key";
        String filePath = "src/test/resources/testfile.txt";
        PutObjectResponse mockResponse = PutObjectResponse.builder().eTag("etag").build();
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(mockResponse);

        // Act
        String etag = s3Service.uploadFile(key, filePath);

        // Assert
        assertEquals("etag", etag);
    }

    @Test
    void deleteFile() {
        // Arrange
        String key = "test/key";
        DeleteObjectResponse mockResponse = DeleteObjectResponse.builder().build();
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(mockResponse);

        // Act
        s3Service.deleteFile(key);

        // Assert
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void downloadFile() throws IOException {
        // Arrange
        String key = "test/key";
        String downloadPath = "src/test/resources/downloadedfile.txt";
        String fileContent = "test content";

        // Mock the InputStream to return the file content
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        GetObjectResponse mockResponse = GetObjectResponse.builder().build();

        // Use Mockito to mock the S3Client.getObject() method
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenAnswer(invocation -> {
                    // Return a ResponseInputStream with a mocked InputStream
                    return new ResponseInputStream<>(mockResponse, AbortableInputStream.create(inputStream));
                });

        // Act
        File file = s3Service.downloadFile(key, downloadPath);

        // Assert
        assertTrue(file.exists());
        String content = new String(Files.readAllBytes(Paths.get(downloadPath)));
        assertEquals(fileContent, content);
    }


    @Test
    void listFiles() {
        // Arrange
        ListObjectsV2Response mockResponse = ListObjectsV2Response.builder()
                .contents(List.of(S3Object.builder().key("folder1/file1.txt").build()))
                .build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class)))
                .thenReturn(mockResponse);

        // Act
        List<S3Service.FileDetails> fileDetails = s3Service.listFiles();

        // Assert
        assertFalse(fileDetails.isEmpty());
        assertEquals("folder1", fileDetails.get(0).getFolderId());
        assertEquals("file1.txt", fileDetails.get(0).getFileName());
        assertEquals("/files/download/folder1?fileName=file1.txt", fileDetails.get(0).getDownloadRoute());
    }
}
