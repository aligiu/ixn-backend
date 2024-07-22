package com.hng.ixn.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/files")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String bucketName = "ixn-radio";
        String key = file.getOriginalFilename();

        // Convert MultipartFile to File
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convertedFile);

        return s3Service.uploadFile(bucketName, key, convertedFile.getPath());
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("fileName") String fileName) {
        try {
            String bucketName = "ixn-radio";
            String downloadPath = System.getProperty("java.io.tmpdir") + "/" + fileName;

            File file = s3Service.downloadFile(bucketName, fileName, downloadPath);

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