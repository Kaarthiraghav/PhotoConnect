package com.dhanush.portfolio.repository;

import com.dhanush.portfolio.model.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioItem, Long> {
    List<PortfolioItem> findByFeaturedTrueOrderByDisplayOrderAsc();
    List<PortfolioItem> findByCategoryOrderByDisplayOrderAsc(String category);

    @Query("SELECT COUNT(p) FROM PortfolioItem p WHERE p.featured = true")
    long countByFeaturedTrue();

    @Query("SELECT DISTINCT p.category FROM PortfolioItem p WHERE p.category IS NOT NULL")
    List<String> findAllCategories();
}