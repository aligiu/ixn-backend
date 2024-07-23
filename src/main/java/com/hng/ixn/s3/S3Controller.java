package com.hng.ixn.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PreAuthorize("hasRole('ADMIN')")  // only admins can upload files
    @PostMapping("/upload/{bucketId}")
    public ResponseEntity<String> uploadFile(@PathVariable int bucketId, @RequestParam("file") MultipartFile file) {
        try {
            String key = bucketId + "/" + file.getOriginalFilename(); // Use the id and the file name for the key

            // Convert MultipartFile to File
            File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convertedFile);

            String eTag = s3Service.uploadFile(key, convertedFile.getPath());
            return ResponseEntity.ok("File uploaded successfully. ETag: " + eTag);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{bucketId}")
    public ResponseEntity<?> downloadFile(@PathVariable int bucketId, @RequestParam("fileName") String fileName) {
        try {
            String key = bucketId + "/" + fileName;
            String downloadPath = System.getProperty("java.io.tmpdir") + "/" + fileName;

            File file = s3Service.downloadFile(key, downloadPath);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found in the local directory.");
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);
        } catch (NoSuchKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File with key '" + fileName + "' not found in S3 bucket.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading file: " + e.getMessage());
        }
    }

}
