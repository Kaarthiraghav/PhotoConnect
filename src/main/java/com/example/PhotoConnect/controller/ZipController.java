package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.service.ZipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/zip")
@Slf4j
public class ZipController {

    private final ZipService zipService;

    public ZipController(ZipService zipService) {
        this.zipService = zipService;
    }

    /**
     * Create ZIP from file paths
     */
    @PostMapping("/create-from-paths")
    public ResponseEntity<byte[]> createZipFromPaths(
            @RequestBody List<String> filePaths,
            @RequestParam(required = false, defaultValue = "archive") String zipName) {

        try {
            log.info("Creating ZIP from {} files", filePaths.size());

            // Validate files
            if (!zipService.validateFilesExist(filePaths)) {
                return ResponseEntity.badRequest()
                        .body(("Some files do not exist").getBytes());
            }

            // Create ZIP
            byte[] zipContent = zipService.createZipArchiveFromPaths(filePaths, zipName);

            // Return ZIP file
            String fileName = zipService.generateZipFileName(zipName);
            @SuppressWarnings("null")
            ResponseEntity<byte[]> response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(zipContent.length)
                    .body(zipContent);
            return response;

        } catch (IOException e) {
            log.error("Error creating ZIP archive", e);
            return ResponseEntity.internalServerError()
                    .body(("Error creating ZIP: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ZIP service is working");
    }

    /**
     * Generate ZIP file name
     */
    @GetMapping("/generate-name")
    public ResponseEntity<String> generateZipName(@RequestParam(required = false) String baseName) {
        String fileName = zipService.generateZipFileName(baseName);
        return ResponseEntity.ok(fileName);
    }
}