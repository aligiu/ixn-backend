package com.hng.ixn.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;


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
        String key = "test/upload/key";
        String filePath = "path/to/file.txt";
        String eTag = "mockETag";

        // Mock the PutObjectResponse to return a specific eTag
        PutObjectResponse mockResponse = PutObjectResponse.builder().eTag(eTag).build();
        when(s3Client.putObject(any(PutObjectRequest.class), any(Path.class))).thenReturn(
                mockResponse);

        // Act
        String resultETag = s3Service.uploadFile(key, filePath);

        // Assert
        assertEquals(eTag, resultETag);
        verify(s3Client).putObject(any(PutObjectRequest.class), any(Path.class));
    }

    @Test
    void deleteFile() {
        // Arrange
        String key = "test/key";
        DeleteObjectResponse mockResponse = DeleteObjectResponse.builder().build();
        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(mockResponse);

        // Act
        s3Service.deleteFile(key);

        // Assert
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void downloadFile() throws IOException {
        // Arrange
        String key = "test/key";
        // Use a temporary file for testing
        File tempFile = File.createTempFile("downloadedfile", ".txt");
        tempFile.deleteOnExit(); // Ensure the file is deleted after the test

        String fileContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        GetObjectResponse mockResponse = GetObjectResponse.builder().build();

        // Mock the S3Client to return a ResponseInputStream with the mocked InputStream
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(
                new ResponseInputStream<>(mockResponse, AbortableInputStream.create(inputStream)));

        // Act
        File file = s3Service.downloadFile(key, tempFile.getAbsolutePath());

        // Assert
        assertTrue(file.exists());
        String content = new String(Files.readAllBytes(file.toPath()));
        assertEquals(fileContent, content);
    }


    @Test
    void listAllFiles() {
        // Arrange
        ListObjectsV2Response mockResponse = ListObjectsV2Response.builder().contents(
                List.of(S3Object.builder().key("folder1/file1.txt").build())).build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(mockResponse);

        // Act
        List<FileDetails> fileDetails = s3Service.listFiles();

        // Assert
        assertFalse(fileDetails.isEmpty());
        assertEquals("folder1", fileDetails.get(0).getFolderId());
        assertEquals("file1.txt", fileDetails.get(0).getFileName());
        assertEquals("/files/download/folder1?fileName=file1.txt",
                fileDetails.get(0).getDownloadRoute());
    }

    @Test
    public void listFolderFiles() {
        // Mock S3 objects
        S3Object s3Object1 = mock(S3Object.class);
        S3Object s3Object2 = mock(S3Object.class);
        S3Object s3Object3 = mock(S3Object.class);

        // Setup S3 object keys
        when(s3Object1.key()).thenReturn("1/file1.txt");
        when(s3Object2.key()).thenReturn("1/file2.txt");
        when(s3Object3.key()).thenReturn("2/file3.txt");

        // Mock the S3 response
        ListObjectsV2Response mockResponse = mock(ListObjectsV2Response.class);
        when(mockResponse.contents()).thenReturn(Arrays.asList(s3Object1, s3Object2, s3Object3));

        // Mock the S3 client to return the mocked response
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(mockResponse);

        // Call the method under test
        List<FileDetails> result = s3Service.listFiles("1");

        // Verify the results
        assertEquals(2, result.size());
        assertThat(result).extracting("folderId").containsExactlyInAnyOrder("1", "1");
        assertThat(result).extracting("fileName").containsExactlyInAnyOrder("file1.txt", "file2.txt");
    }
}
