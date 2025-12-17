package com.dhanush.portfolio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    // Inner class for Upload Response - MUST be public and static
    public static class UploadResponse {
        private final String publicId;
        private final String url;
        private final String secureUrl;
        private final String format;
        private final Long bytes;
        private final String folder;

        public UploadResponse(String publicId, String url, String secureUrl,
                              String format, Long bytes, String folder) {
            this.publicId = publicId;
            this.url = url;
            this.secureUrl = secureUrl;
            this.format = format;
            this.bytes = bytes;
            this.folder = folder;
        }

        // Getters
        public String getPublicId() { return publicId; }
        public String getUrl() { return url; }
        public String getSecureUrl() { return secureUrl; }
        public String getFormat() { return format; }
        public Long getBytes() { return bytes; }
        public String getFolder() { return folder; }
    }

    /**
     * Upload a photo to Cloudinary
     */
    public UploadResponse uploadPhoto(MultipartFile file, String folderPath) throws IOException {
        File uploadedFile = convertMultiPartToFile(file);
        String publicId = folderPath + "/" + UUID.randomUUID() + "_" +
                sanitizeFileName(file.getOriginalFilename());

        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "folder", folderPath,
                "public_id", publicId,
                "overwrite", false,
                "resource_type", "auto",
                "use_filename", true,
                "unique_filename", true
        );

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(uploadedFile, uploadOptions);

            return new UploadResponse(
                    (String) uploadResult.get("public_id"),
                    (String) uploadResult.get("url"),
                    (String) uploadResult.get("secure_url"),
                    (String) uploadResult.get("format"),
                    ((Number) uploadResult.get("bytes")).longValue(),
                    (String) uploadResult.get("folder")
            );
        } finally {
            // Clean up the temporary file
            Files.deleteIfExists(uploadedFile.toPath());
        }
    }

    /**
     * Upload a photo with transformations
     */
    public UploadResponse uploadWithTransformations(MultipartFile file, String folderPath,
                                                    Map<String, Object> transformations) throws IOException {
        File uploadedFile = convertMultiPartToFile(file);
        String publicId = UUID.randomUUID() + "_" + sanitizeFileName(file.getOriginalFilename());

        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("folder", folderPath);
        uploadOptions.put("public_id", publicId);

        // Build transformation string
        if (transformations != null && !transformations.isEmpty()) {
            StringBuilder transformationString = new StringBuilder();
            transformations.forEach((key, value) -> {
                transformationString.append(key).append("_").append(value).append(",");
            });
            // Remove trailing comma
            if (transformationString.length() > 0) {
                transformationString.deleteCharAt(transformationString.length() - 1);
            }
            uploadOptions.put("transformation", transformationString.toString());
        }

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(uploadedFile, uploadOptions);

            return new UploadResponse(
                    (String) uploadResult.get("public_id"),
                    (String) uploadResult.get("url"),
                    (String) uploadResult.get("secure_url"),
                    (String) uploadResult.get("format"),
                    ((Number) uploadResult.get("bytes")).longValue(),
                    (String) uploadResult.get("folder")
            );
        } finally {
            Files.deleteIfExists(uploadedFile.toPath());
        }
    }

    /**
     * Delete a photo from Cloudinary
     */
    public Map<String, Object> deletePhoto(String publicId) throws IOException {
        Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return result;
    }

    /**
     * Delete multiple photos
     */
    public void deleteMultiplePhotos(List<String> publicIds) {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }

        publicIds.forEach(publicId -> {
            try {
                deletePhoto(publicId);
                log.info("Deleted photo: {}", publicId);
            } catch (IOException e) {
                log.error("Failed to delete photo: {}", publicId, e);
            }
        });
    }

    /**
     * Generate URL with transformations
     */
    public String generateTransformedUrl(String publicId, Map<String, String> transformations) {
        if (transformations == null || transformations.isEmpty()) {
            return cloudinary.url().generate(publicId);
        }

        Transformation transformation = new Transformation();
        transformations.forEach((key, value) -> {
            String keyLower = key.toLowerCase();
            switch (keyLower) {
                case "width":
                    transformation.width(Integer.parseInt(value));
                    break;
                case "height":
                    transformation.height(Integer.parseInt(value));
                    break;
                case "crop":
                    transformation.crop(value);
                    break;
                case "gravity":
                    transformation.gravity(value);
                    break;
                case "quality":
                    transformation.quality(value);
                    break;
                case "radius":
                    transformation.radius(value);
                    break;
                case "effect":
                    transformation.effect(value);
                    break;
                default:
                    transformation.rawTransformation(key + "_" + value);
                    break;
            }
        });

        return cloudinary.url().transformation(transformation).generate(publicId);
    }

    /**
     * Generate thumbnail URL
     */
    public String generateThumbnailUrl(String publicId, int width, int height) {
        return cloudinary.url()
                .transformation(new Transformation()
                        .width(width)
                        .height(height)
                        .crop("fill")
                        .gravity("auto")
                        .quality("auto:good"))
                .generate(publicId);
    }

    /**
     * Get image information from Cloudinary
     */
    public Map<String, String> getImageInfo(String publicId) {
        Map<String, String> info = new HashMap<>();

        try {
            Map<String, Object> result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());

            // Add all available information
            if (result.containsKey("public_id")) {
                info.put("public_id", (String) result.get("public_id"));
            }
            if (result.containsKey("format")) {
                info.put("format", (String) result.get("format"));
            }
            if (result.containsKey("resource_type")) {
                info.put("resource_type", (String) result.get("resource_type"));
            }
            if (result.containsKey("bytes")) {
                info.put("bytes", String.valueOf(result.get("bytes")));
            }
            if (result.containsKey("width")) {
                info.put("width", String.valueOf(result.get("width")));
            }
            if (result.containsKey("height")) {
                info.put("height", String.valueOf(result.get("height")));
            }
            if (result.containsKey("url")) {
                info.put("url", (String) result.get("url"));
            }
            if (result.containsKey("secure_url")) {
                info.put("secure_url", (String) result.get("secure_url"));
            }
            if (result.containsKey("created_at")) {
                info.put("created_at", String.valueOf(result.get("created_at")));
            }

        } catch (Exception e) {
            log.error("Error getting image info for {}: {}", publicId, e.getMessage());
            info.put("error", e.getMessage());
        }

        return info;
    }

    /**
     * Create folder path
     */
    public String createFolderPath(String baseFolder, String... subfolders) {
        StringBuilder path = new StringBuilder(baseFolder);
        for (String subfolder : subfolders) {
            if (subfolder != null && !subfolder.trim().isEmpty()) {
                path.append("/").append(sanitizeFolderName(subfolder));
            }
        }
        return path.toString();
    }

    /**
     * List images in a folder
     */
    public List<Map<String, Object>> listImagesInFolder(String folderPath) {
        try {
            Map<String, Object> options = ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", folderPath,
                    "max_results", 500
            );

            Map<String, Object> result = cloudinary.api().resources(options);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> resources = (List<Map<String, Object>>) result.get("resources");

            return resources != null ? resources : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error listing images in folder {}: {}", folderPath, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Upload multiple files
     */
    public List<UploadResponse> uploadMultiplePhotos(List<MultipartFile> files, String folderPath) throws IOException {
        List<UploadResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                UploadResponse response = uploadPhoto(file, folderPath);
                responses.add(response);
            } catch (IOException e) {
                log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                throw e;
            }
        }

        return responses;
    }

    /**
     * Create download URL for archive
     */
    public String createArchiveDownloadUrl(List<String> publicIds, String format, String folder) {
        try {
            // For simple cases, return the first image URL with download flag
            if (publicIds == null || publicIds.isEmpty()) {
                return null;
            }

            // Create a transformation with attachment flag for download
            // Using rawTransformation to avoid method not found errors
            String transformationString = "f_" + (format != null ? format : "jpg") + ",fl_attachment";

            Transformation transformation = new Transformation()
                    .rawTransformation(transformationString);

            return cloudinary.url()
                    .transformation(transformation)
                    .generate(publicIds.get(0));

        } catch (Exception e) {
            log.error("Error creating download URL: {}", e.getMessage());
            return null;
        }
    }

    // Private helper methods

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = "upload_" + System.currentTimeMillis();
        }

        Path tempFile = Files.createTempFile("upload_", "_" + fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile.toFile())) {
            fos.write(file.getBytes());
        }
        return tempFile.toFile();
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "file";

        // Remove extension
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex);
        }

        // Replace special characters
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String sanitizeFolderName(String folderName) {
        return folderName.trim()
                .replaceAll("[^a-zA-Z0-9_-]", "_")
                .toLowerCase();
    }

    /**
     * Validate Cloudinary credentials
     */
    public boolean validateCredentials() {
        try {
            // Simple API call to verify credentials
            cloudinary.api().ping(ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            log.error("Cloudinary credentials validation failed", e);
            return false;
        }
    }

    /**
     * Get Cloudinary account usage
     */
    public Map<String, Object> getAccountUsage() {
        try {
            return cloudinary.api().usage(ObjectUtils.emptyMap());
        } catch (Exception e) {
            log.error("Error getting account usage: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Simple URL generation
     */
    public String generateUrl(String publicId) {
        try {
            return cloudinary.url().generate(publicId);
        } catch (Exception e) {
            log.error("Error generating URL for {}: {}", publicId, e.getMessage());
            return null;
        }
    }

    /**
     * Get Cloudinary configuration info
     */
    public Map<String, String> getConfigInfo() {
        Map<String, String> config = new HashMap<>();
        try {
            config.put("cloud_name", cloudName);
            if (cloudinary.config.apiKey != null) {
                String apiKey = cloudinary.config.apiKey;
                String maskedKey = "***" + apiKey.substring(Math.max(0, apiKey.length() - 4));
                config.put("api_key", maskedKey);
            }
            config.put("api_secret", "***");
            config.put("secure", "true");
        } catch (Exception e) {
            log.error("Error getting config info: {}", e.getMessage());
        }
        return config;
    }

    /**
     * Generate download URL for a single image
     */
    public String generateDownloadUrl(String publicId, String format) {
        try {
            // Using rawTransformation to avoid method not found errors
            String transformationString = "f_" + (format != null ? format : "jpg") + ",fl_attachment";

            Transformation transformation = new Transformation()
                    .rawTransformation(transformationString);

            return cloudinary.url()
                    .transformation(transformation)
                    .generate(publicId);
        } catch (Exception e) {
            log.error("Error generating download URL: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Check if image exists on Cloudinary
     */
    public boolean imageExists(String publicId) {
        try {
            Map<String, Object> info = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return info != null && info.containsKey("public_id");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get image dimensions
     */
    public Map<String, Integer> getImageDimensions(String publicId) {
        Map<String, Integer> dimensions = new HashMap<>();
        try {
            Map<String, Object> info = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            if (info.containsKey("width") && info.containsKey("height")) {
                dimensions.put("width", (Integer) info.get("width"));
                dimensions.put("height", (Integer) info.get("height"));
            }
        } catch (Exception e) {
            log.error("Error getting image dimensions: {}", e.getMessage());
        }
        return dimensions;
    }

    /**
     * Create ZIP archive download URL for multiple images
     */
    public String createZipArchiveDownloadUrl(List<String> publicIds) {
        try {
            if (publicIds == null || publicIds.isEmpty()) {
                return null;
            }

            // For single image, just return its download URL
            if (publicIds.size() == 1) {
                return generateDownloadUrl(publicIds.get(0), null);
            }

            // Build Cloudinary archive URL
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://res.cloudinary.com/")
                    .append(cloudName)
                    .append("/image/generate_archive");

            // Build query parameters
            List<String> params = new ArrayList<>();
            params.add("api_key=" + cloudinary.config.apiKey);
            params.add("mode=download");
            params.add("timestamp=" + System.currentTimeMillis());

            // Add all public IDs
            for (int i = 0; i < Math.min(publicIds.size(), 100); i++) {
                params.add("public_ids[]=" + publicIds.get(i));
            }

            urlBuilder.append("?");
            urlBuilder.append(String.join("&", params));

            return urlBuilder.toString();

        } catch (Exception e) {
            log.error("Error creating ZIP archive URL: {}", e.getMessage());
            return null;
        }
    }
}