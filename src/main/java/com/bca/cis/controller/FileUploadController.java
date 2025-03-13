package com.bca.cis.controller;


import bca.hcp.impl.HcpImpl;
import com.bca.cis.entity.FileManager;
import com.bca.cis.repository.FileUploadRepository;
import com.bca.cis.util.QrUploadHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class FileUploadController {

    private final FileUploadRepository fileUploadRepository;

    private String hcpToken = "abcdefg";
    private String hcpUrl = "https://google.com";

    @PostMapping(value = "/upload-multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            byte[] fileBytes = multipartFile.getBytes();
            byte[] qrCode = QrUploadHelper.generateNewQRCodeWithAvatar("123123123", fileBytes);

            FileManager fileManager = new FileManager();
            fileManager.setFileName(multipartFile.getOriginalFilename());
            fileManager.setFileData(fileBytes); // Pastikan ini menggunakan byte[]
            fileManager.setQrCode(qrCode); // Pastikan ini menggunakan byte[]
            fileManager.setFileSize(fileBytes.length);
            fileManager.setUploadedAt(LocalDateTime.now());

            fileUploadRepository.save(fileManager);

            try {
                HcpImpl newUpload = new HcpImpl();
                newUpload.uploadFile("Banana", fileBytes, "http://google.com/", "abc123");
            } catch (Exception e) {
                log.error("FAILED UPLAOD TO HCP : {}", e.getMessage());
            }

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<FileManager> getFileMetadata(@PathVariable Long id) {
        Optional<FileManager> fileManagerOptional = fileUploadRepository.findById(id);
        if (fileManagerOptional.isPresent()) {
            return ResponseEntity.ok(fileManagerOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/file/hcps/{id}")
    public ResponseEntity<InputStreamResource> getFileMetadatazz(@PathVariable Long id) {
        Optional<FileManager> fileManagerOptional = fileUploadRepository.findById(id);

        if (fileManagerOptional.isPresent()) {
            FileManager fileManager = fileManagerOptional.get();

            try {
                InputStream inputStream = new ByteArrayInputStream(fileManager.getFileData());
                InputStreamResource resource = new InputStreamResource(inputStream);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileManager.getFileName() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/file/hcp/{id}")
    public ResponseEntity<?> getFileMetadataHcp(@PathVariable String id, HttpServletRequest request) {
        HcpImpl newHcp = new HcpImpl();

        try {
            InputStream files = newHcp.downloadFile(id, hcpUrl, hcpToken, false, request, 5000L);
            Map<String, String> response = new HashMap<>();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("Error to get hcp: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error");
        }

    }


    @GetMapping("/file/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id, @RequestParam String type) {
        Optional<FileManager> fileManagerOptional = fileUploadRepository.findById(id);
        if (fileManagerOptional.isPresent()) {
            FileManager fileManager = fileManagerOptional.get();

            // Membuat response header untuk unduhan
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(fileManager.getFileName())
                    .build());

            return new ResponseEntity<>(type.equals("img") ? fileManager.getFileData() : fileManager.getQrCode(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
