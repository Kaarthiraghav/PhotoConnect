package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/cloudinary")
@Tag(name = "Cloudinary Operations", description = "APIs for Cloudinary upload/delete operations")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    @Operation(summary = "Upload file to Cloudinary")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) throws IOException {

        CloudinaryService.UploadResponse response = cloudinaryService.uploadPhoto(file, folder);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("publicId", response.getPublicId());
        result.put("url", response.getUrl());
        result.put("secureUrl", response.getSecureUrl());
        result.put("format", response.getFormat());
        result.put("bytes", response.getBytes());
        result.put("folder", response.getFolder());
        result.put("message", "File uploaded successfully");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload-with-transformations")
    @Operation(summary = "Upload file with transformations")
    public ResponseEntity<Map<String, Object>> uploadWithTransformations(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder,
            @RequestParam Map<String, String> transformations) throws IOException {

        // Convert Map<String, String> to Map<String, Object> for service
        Map<String, Object> transformMap = new HashMap<>();
        if (transformations != null) {
            transformMap.putAll(transformations);
        }

        CloudinaryService.UploadResponse response =
                cloudinaryService.uploadWithTransformations(file, folder, transformMap);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("publicId", response.getPublicId());
        result.put("url", response.getUrl());
        result.put("secureUrl", response.getSecureUrl());
        result.put("format", response.getFormat());
        result.put("bytes", response.getBytes());
        result.put("folder", response.getFolder());
        result.put("transformations", transformations);
        result.put("message", "File uploaded with transformations successfully");

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{publicId}")
    @Operation(summary = "Delete file from Cloudinary")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String publicId) {
        try {
            Map<String, Object> deleteResult = cloudinaryService.deletePhoto(publicId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("publicId", publicId);
            response.put("result", deleteResult);
            response.put("message", "File deleted successfully");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Delete failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/delete-multiple")
    @Operation(summary = "Delete multiple photos")
    public ResponseEntity<Map<String, Object>> deleteMultiplePhotos(@RequestBody List<String> publicIds) {
        cloudinaryService.deleteMultiplePhotos(publicIds);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("deletedCount", publicIds.size());
        response.put("publicIds", publicIds);
        response.put("message", "Photos deletion initiated");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/folder")
    @Operation(summary = "Create folder structure")
    public ResponseEntity<Map<String, String>> createFolder(@RequestParam String folderPath) {
        String path = cloudinaryService.createFolderPath(folderPath);

        Map<String, String> response = new HashMap<>();
        response.put("folderPath", path);
        response.put("message", "Folder path created. Upload files to this path to create the folder.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/generate-url")
    @Operation(summary = "Generate transformed URL")
    public ResponseEntity<Map<String, String>> generateUrl(
            @RequestParam String publicId,
            @RequestParam(required = false) Map<String, String> transformations) {

        String url;
        if (transformations != null && !transformations.isEmpty()) {
            url = cloudinaryService.generateTransformedUrl(publicId, transformations);
        } else {
            url = cloudinaryService.generateThumbnailUrl(publicId, 300, 300);
        }

        Map<String, String> response = new HashMap<>();
        response.put("publicId", publicId);
        response.put("url", url);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info/{publicId}")
    @Operation(summary = "Get image info from Cloudinary")
    public ResponseEntity<Map<String, Object>> getImageInfo(@PathVariable String publicId) {
        Map<String, String> info = cloudinaryService.getImageInfo(publicId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("info", info);
        response.put("message", "Image information retrieved successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-multiple")
    @Operation(summary = "Upload multiple files")
    public ResponseEntity<Map<String, Object>> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("folder") String folder) throws IOException {

        List<CloudinaryService.UploadResponse> responses = cloudinaryService.uploadMultiplePhotos(files, folder);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("uploadedCount", responses.size());
        result.put("responses", responses);
        result.put("message", "Files uploaded successfully");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/list-folder/{folderPath}")
    @Operation(summary = "List images in folder")
    public ResponseEntity<Map<String, Object>> listFolderImages(@PathVariable String folderPath) {
        List<Map<String, Object>> images = cloudinaryService.listImagesInFolder(folderPath);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("folderPath", folderPath);
        response.put("imageCount", images.size());
        response.put("images", images);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate Cloudinary credentials")
    public ResponseEntity<Map<String, Boolean>> validateCredentials() {
        boolean isValid = cloudinaryService.validateCredentials();

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/config")
    @Operation(summary = "Get Cloudinary configuration")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = cloudinaryService.getConfigInfo();
        return ResponseEntity.ok(config);
    }
}