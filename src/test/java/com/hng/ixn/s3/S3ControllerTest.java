package com.hng.ixn.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class S3ControllerTest {

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private S3Controller s3Controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadFile_Success() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain",
                "some content".getBytes());

        when(s3Service.uploadFile(anyString(), anyString())).thenReturn("etag12345");

        ResponseEntity<String> response = s3Controller.uploadFile("folder1", mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("File uploaded successfully. ETag: etag12345"));
        verify(s3Service, times(1)).uploadFile(anyString(), anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadFile_EmptyFile() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        ResponseEntity<String> response = s3Controller.uploadFile("folder1", mockFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No file provided.", response.getBody());
        verify(s3Service, times(0)).uploadFile(anyString(), anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadFile_shouldReturnInternalServerErrorOnIOException() throws IOException {
        // Arrange
        String folderId = "test-folder";
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "some content".getBytes());

        // Create a spy for the MultipartFile to simulate IOException during transferTo
        MockMultipartFile spyFile = spy(mockFile);
        doThrow(new IOException("File transfer failed")).when(spyFile).transferTo(any(File.class));

        // Act
        ResponseEntity<String> response = s3Controller.uploadFile(folderId, spyFile);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error uploading file: File transfer failed", response.getBody());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFile_Success() {
        doNothing().when(s3Service).deleteFile(anyString());

        ResponseEntity<String> response = s3Controller.deleteFile("folder1", "test.txt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File deleted successfully.", response.getBody());
        verify(s3Service, times(1)).deleteFile(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFile_NoSuchKeyException() {
        // Simulate NoSuchKeyException by throwing it from the mock
        doThrow(NoSuchKeyException.builder().message("Key not found").build()).when(s3Service)
                .deleteFile(anyString());

        ResponseEntity<String> response = s3Controller.deleteFile("folder1", "test.txt");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("File with key 'test.txt' not found in S3 bucket."));
        verify(s3Service, times(1)).deleteFile(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFile_Exception() {
        doThrow(new RuntimeException("Unexpected error")).when(s3Service).deleteFile(anyString());

        ResponseEntity<String> response = s3Controller.deleteFile("folder1", "test.txt");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Error deleting file: " + "Unexpected error"));
        verify(s3Service, times(1)).deleteFile(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void downloadFile_NoSuchKeyException() throws IOException {
        // Simulate NoSuchKeyException by throwing it from the mock
        doThrow(NoSuchKeyException.builder().message("Key not found").build()).when(s3Service)
                .downloadFile(anyString(), anyString());

        // Perform the downloadFile call
        ResponseEntity<?> response = s3Controller.downloadFile("folder1", "test.txt");

        // Verify that the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Check that the response body contains the correct message
        // Convert body to String and ensure it contains the message
        assertTrue(response.getBody() != null && response.getBody().toString()
                .contains("File with key 'test.txt' not found in S3 bucket."));

        // Verify that downloadFile was called once
        verify(s3Service, times(1)).downloadFile(anyString(), anyString());
    }

    @Test
    void downloadFile_IOException() throws IOException {
        // Simulate IOException by throwing it from the mock
        doThrow(new IOException("Simulated IOException")).when(s3Service)
                .downloadFile(anyString(), anyString());

        // Perform the downloadFile call
        ResponseEntity<?> response = s3Controller.downloadFile("folder1", "test.txt");

        // Verify that the response status is INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Check that the response body contains the correct message
        assertTrue(response.getBody() != null && response.getBody().toString()
                .contains("Error downloading file: Simulated IOException"));

        // Verify that downloadFile was called once
        verify(s3Service, times(1)).downloadFile(anyString(), anyString());
    }


    @Test
    void downloadFile_FileNotFound() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);
        when(s3Service.downloadFile(anyString(), anyString())).thenReturn(mockFile);

        ResponseEntity<?> response = s3Controller.downloadFile("folder1", "test.txt");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("File not found in the local directory.", response.getBody());
        verify(s3Service, times(1)).downloadFile(anyString(), anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void downloadFile_Success() throws IOException {
        // Arrange
        String folderId = "folder1";
        String fileName = "test.txt";
        String key = folderId + "/" + fileName;
        String downloadPath = System.getProperty("java.io.tmpdir") + "/" + fileName;

        // Create a mock File object and write content to it
        File mockFile = new File(downloadPath);
        byte[] fileContent = "This is a test file content".getBytes();
        try (FileOutputStream fos = new FileOutputStream(mockFile)) {
            fos.write(fileContent);
        }

        // Mock the behavior of s3Service
        when(s3Service.downloadFile(eq(key), eq(downloadPath))).thenReturn(mockFile);

        // Act
        ResponseEntity<?> response = s3Controller.downloadFile(folderId, fileName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).contains("attachment;filename=" + fileName));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals(fileContent.length, response.getHeaders().getContentLength());

        // Read the response body
        InputStreamResource resource = (InputStreamResource) response.getBody();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent)) {
            byte[] responseBody = byteArrayInputStream.readAllBytes();
            assertArrayEquals(fileContent, responseBody);
        }

        verify(s3Service, times(1)).downloadFile(eq(key), eq(downloadPath));
    }

    @Test
    void listFilesAll_Success() {
        List<FileDetails> mockFiles = Collections.singletonList(
                new FileDetails("file1", "folder1/file1", "1024L"));

        when(s3Service.listFiles()).thenReturn(mockFiles);

        ResponseEntity<List<FileDetails>> response = s3Controller.listFilesAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFiles, response.getBody());
        verify(s3Service, times(1)).listFiles();
    }

    @Test
    void listFilesOfFolder_Success() {
        List<FileDetails> mockFiles = Collections.singletonList(
                new FileDetails("file1", "folder1/file1", "1024L"));

        when(s3Service.listFiles("folder1")).thenReturn(mockFiles);

        ResponseEntity<List<FileDetails>> response = s3Controller.listFilesOfFolder(
                "folder1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFiles, response.getBody());
        verify(s3Service, times(1)).listFiles("folder1");
    }
}
