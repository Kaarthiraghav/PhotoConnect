package com.example.PhotoConnect.service;

import com.example.PhotoConnect.model.PortfolioItem;
import com.example.PhotoConnect.model.Photo;
import com.example.PhotoConnect.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final CloudinaryService cloudinaryService;

    public List<PortfolioItem> getAllPortfolioItems() {
        return portfolioRepository.findAll();
    }

    @SuppressWarnings("null")
    public Optional<PortfolioItem> getPortfolioItemById(Long id) {
        return portfolioRepository.findById(id);
    }

    @Transactional
    @SuppressWarnings("null")
    public PortfolioItem createPortfolioItem(PortfolioItem portfolioItem, List<MultipartFile> files) {
        try {
            // Save portfolio item first
            PortfolioItem savedItem = portfolioRepository.save(portfolioItem);

            // Upload photos if provided
            if (files != null && !files.isEmpty()) {
                uploadPhotosToPortfolio(savedItem, files);
            }

            return savedItem;
        } catch (Exception e) {
            log.error("Error creating portfolio item", e);
            throw new RuntimeException("Failed to create portfolio item: " + e.getMessage());
        }
    }

    private void uploadPhotosToPortfolio(PortfolioItem portfolioItem, List<MultipartFile> files) throws IOException {
        String folderPath = "portfolio/" + portfolioItem.getId();
        List<Photo> uploadedPhotos = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            CloudinaryService.UploadResponse uploadResponse = cloudinaryService.uploadPhoto(file, folderPath);

            Photo photo = createPhotoFromUpload(uploadResponse, i);
            photo.setPortfolioItem(portfolioItem);
            uploadedPhotos.add(photo);
        }

        portfolioItem.getPhotos().addAll(uploadedPhotos);
        portfolioRepository.save(portfolioItem);
    }

    private Photo createPhotoFromUpload(CloudinaryService.UploadResponse response, int order) {
        Photo photo = new Photo();
        photo.setPublicId(response.getPublicId());
        photo.setUrl(response.getUrl());
        photo.setSecureUrl(response.getSecureUrl());
        photo.setFormat(response.getFormat());
        photo.setBytes(response.getBytes());
        photo.setDisplayOrder(order);
        photo.setUploadedAt(LocalDateTime.now());
        return photo;
    }

    @Transactional
    @SuppressWarnings("null")
    public PortfolioItem updatePortfolioItem(Long id, PortfolioItem portfolioItem) {
        return portfolioRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setTitle(portfolioItem.getTitle());
                    existingItem.setDescription(portfolioItem.getDescription());
                    existingItem.setCategory(portfolioItem.getCategory());
                    existingItem.setTags(portfolioItem.getTags());
                    existingItem.setFeatured(portfolioItem.isFeatured());
                    existingItem.setDisplayOrder(portfolioItem.getDisplayOrder());
                    existingItem.setUpdatedAt(LocalDateTime.now());
                    return portfolioRepository.save(existingItem);
                })
                .orElseThrow(() -> new RuntimeException("Portfolio item not found with id: " + id));
    }

    @Transactional
    @SuppressWarnings("null")
    public void deletePortfolioItem(Long id) {
        portfolioRepository.findById(id).ifPresentOrElse(
                portfolioItem -> {
                    // Extract public IDs from photos
                    List<String> publicIds = new ArrayList<>();
                    for (Photo photo : portfolioItem.getPhotos()) {
                        publicIds.add(photo.getPublicId());
                    }

                    // Delete from Cloudinary
                    if (!publicIds.isEmpty()) {
                        cloudinaryService.deleteMultiplePhotos(publicIds);
                    }

                    // Delete from database
                    portfolioRepository.delete(portfolioItem);
                    log.info("Deleted portfolio item with id: {}", id);
                },
                () -> {
                    throw new RuntimeException("Portfolio item not found with id: " + id);
                }
        );
    }

    @Transactional
    @SuppressWarnings("null")
    public PortfolioItem addPhotosToPortfolio(Long portfolioId, List<MultipartFile> files) {
        PortfolioItem portfolioItem = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio item not found with id: " + portfolioId));

        try {
            String folderPath = "portfolio/" + portfolioId;
            int startOrder = portfolioItem.getPhotos().size();

            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                CloudinaryService.UploadResponse uploadResponse = cloudinaryService.uploadPhoto(file, folderPath);

                Photo photo = createPhotoFromUpload(uploadResponse, startOrder + i);
                photo.setPortfolioItem(portfolioItem);
                portfolioItem.getPhotos().add(photo);
            }

            return portfolioRepository.save(portfolioItem);
        } catch (Exception e) {
            log.error("Error adding photos to portfolio", e);
            throw new RuntimeException("Failed to add photos: " + e.getMessage());
        }
    }

    public List<PortfolioItem> getFeaturedPortfolioItems() {
        return portfolioRepository.findByFeaturedTrueOrderByDisplayOrderAsc();
    }

    public List<PortfolioItem> getPortfolioItemsByCategory(String category) {
        return portfolioRepository.findByCategoryOrderByDisplayOrderAsc(category);
    }

    public long getPortfolioCount() {
        return portfolioRepository.count();
    }
}