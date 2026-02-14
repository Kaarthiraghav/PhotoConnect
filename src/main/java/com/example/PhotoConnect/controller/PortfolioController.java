package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.PortfolioItem;
import com.example.PhotoConnect.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Tag(name = "Portfolio Management", description = "APIs for managing photographer portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    @Operation(summary = "Get all portfolio items")
    public ResponseEntity<List<PortfolioItem>> getAllPortfolioItems() {
        return ResponseEntity.ok(portfolioService.getAllPortfolioItems());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get portfolio item by ID")
    public ResponseEntity<PortfolioItem> getPortfolioItem(@PathVariable Long id) {
        return portfolioService.getPortfolioItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PortfolioItem> createPortfolioItem(
            @RequestBody PortfolioItem portfolioItem
    ) {
        return ResponseEntity.ok(portfolioService.createPortfolioItem(portfolioItem, null));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update portfolio item")
    public ResponseEntity<PortfolioItem> updatePortfolioItem(
            @PathVariable Long id,
            @RequestBody PortfolioItem portfolioItem) {
        return ResponseEntity.ok(portfolioService.updatePortfolioItem(id, portfolioItem));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete portfolio item")
    public ResponseEntity<Void> deletePortfolioItem(@PathVariable Long id) {
        portfolioService.deletePortfolioItem(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/photos")
    @Operation(summary = "Add photos to portfolio item")
    public ResponseEntity<PortfolioItem> addPhotosToPortfolio(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(portfolioService.addPhotosToPortfolio(id, files));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured portfolio items")
    public ResponseEntity<List<PortfolioItem>> getFeaturedPortfolioItems() {
        return ResponseEntity.ok(portfolioService.getFeaturedPortfolioItems());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get portfolio items by category")
    public ResponseEntity<List<PortfolioItem>> getPortfolioByCategory(@PathVariable String category) {
        return ResponseEntity.ok(portfolioService.getPortfolioItemsByCategory(category));
    }
}