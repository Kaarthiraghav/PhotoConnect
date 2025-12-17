package com.dhanush.portfolio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ZipService {

    // If you have dependencies, autowire them here
    // Example:
    // private final CloudinaryService cloudinaryService;
    // private final PhotoRepository photoRepository;

    // Default constructor - REQUIRED for Spring
    public ZipService() {
        // This default constructor is needed by Spring
        log.debug("ZipService initialized");
    }

    // If you need to inject dependencies, add this constructor too
    /*
    @Autowired
    public ZipService(CloudinaryService cloudinaryService,
                      PhotoRepository photoRepository) {
        this.cloudinaryService = cloudinaryService;
        this.photoRepository = photoRepository;
        log.debug("ZipService initialized with dependencies");
    }
    */

    /**
     * Create ZIP archive from file paths
     */
    public byte[] createZipArchiveFromPaths(List<String> filePaths, String zipName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String filePath : filePaths) {
                Path path = Paths.get(filePath);
                if (Files.exists(path)) {
                    addFileToZip(zos, path);
                } else {
                    log.warn("File not found: {}", filePath);
                }
            }
            zos.finish();
        }

        log.info("Created ZIP archive: {} with {} files", zipName, filePaths.size());
        return baos.toByteArray();
    }

    /**
     * Create ZIP archive from byte arrays (for in-memory files)
     */
    public byte[] createZipArchiveFromBytes(List<byte[]> files, List<String> fileNames, String zipName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (int i = 0; i < files.size(); i++) {
                String fileName = fileNames.get(i);
                byte[] fileContent = files.get(i);

                ZipEntry entry = new ZipEntry(fileName);
                zos.putNextEntry(entry);
                zos.write(fileContent);
                zos.closeEntry();

                log.debug("Added file to ZIP: {}", fileName);
            }
            zos.finish();
        }

        log.info("Created ZIP archive: {} with {} files", zipName, files.size());
        return baos.toByteArray();
    }

    /**
     * Add a single file to ZIP
     */
    private void addFileToZip(ZipOutputStream zos, Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString();
        ZipEntry entry = new ZipEntry(fileName);
        zos.putNextEntry(entry);

        byte[] fileContent = Files.readAllBytes(filePath);
        zos.write(fileContent);
        zos.closeEntry();

        log.debug("Added file to ZIP: {}", fileName);
    }

    /**
     * Validate if files exist before creating ZIP
     */
    public boolean validateFilesExist(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            log.warn("File list is empty or null");
            return false;
        }

        int existingFiles = 0;
        for (String filePath : filePaths) {
            if (Files.exists(Paths.get(filePath))) {
                existingFiles++;
            }
        }

        boolean allExist = existingFiles == filePaths.size();
        log.info("File validation: {}/{} files exist", existingFiles, filePaths.size());
        return allExist;
    }

    /**
     * Get estimated ZIP file size
     */
    public long estimateZipSize(List<String> filePaths) throws IOException {
        long totalSize = 0;

        for (String filePath : filePaths) {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                totalSize += Files.size(path);
            }
        }

        // ZIP compression typically reduces size by 10-50%
        long estimatedSize = (long) (totalSize * 0.8);
        log.debug("Estimated ZIP size: {} bytes (original: {} bytes)", estimatedSize, totalSize);
        return estimatedSize;
    }

    /**
     * Simple ZIP creation for single file
     */
    public byte[] createSingleFileZip(byte[] fileContent, String fileName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry entry = new ZipEntry(fileName);
            zos.putNextEntry(entry);
            zos.write(fileContent);
            zos.closeEntry();
            zos.finish();
        }

        log.info("Created single file ZIP: {}", fileName);
        return baos.toByteArray();
    }

    /**
     * Check if file is already a ZIP
     */
    public boolean isZipFile(String filePath) {
        if (filePath == null || filePath.length() < 4) {
            return false;
        }
        return filePath.toLowerCase().endsWith(".zip");
    }

    /**
     * Get ZIP file name with timestamp
     */
    public String generateZipFileName(String baseName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        if (baseName == null || baseName.trim().isEmpty()) {
            baseName = "archive";
        }
        return baseName + "_" + timestamp + ".zip";
    }

    /**
     * Clean up temporary files (if you create any)
     */
    public void cleanupTempFiles(List<Path> tempFiles) {
        if (tempFiles == null || tempFiles.isEmpty()) {
            return;
        }

        int deletedCount = 0;
        for (Path tempFile : tempFiles) {
            try {
                if (Files.exists(tempFile)) {
                    Files.delete(tempFile);
                    deletedCount++;
                    log.debug("Deleted temp file: {}", tempFile);
                }
            } catch (IOException e) {
                log.error("Failed to delete temp file: {}", tempFile, e);
            }
        }

        log.info("Cleaned up {} temporary files", deletedCount);
    }
}