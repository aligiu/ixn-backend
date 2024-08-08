package com.hng.ixn.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void listFilesAll_Success() {
        List<S3Service.FileDetails> mockFiles = Collections.singletonList(
                new S3Service.FileDetails("file1", "folder1/file1", "1024L"));

        when(s3Service.listFiles()).thenReturn(mockFiles);

        ResponseEntity<List<S3Service.FileDetails>> response = s3Controller.listFilesAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFiles, response.getBody());
        verify(s3Service, times(1)).listFiles();
    }

    @Test
    void listFilesOfFolder_Success() {
        List<S3Service.FileDetails> mockFiles = Collections.singletonList(
                new S3Service.FileDetails("file1", "folder1/file1", "1024L"));

        when(s3Service.listFiles("folder1")).thenReturn(mockFiles);

        ResponseEntity<List<S3Service.FileDetails>> response = s3Controller.listFilesOfFolder(
                "folder1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFiles, response.getBody());
        verify(s3Service, times(1)).listFiles("folder1");
    }
}
