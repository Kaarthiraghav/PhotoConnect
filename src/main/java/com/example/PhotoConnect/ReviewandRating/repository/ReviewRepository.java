package com.example.PhotoConnect.ReviewandRating.repository;

import com.example.PhotoConnect.ReviewandRating.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPhotographerIdAndIsFlaggedFalseOrderByCreatedAtDesc(Long photographerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.photographerId = :id")
    Double getAverageRating(@Param("id") Long id);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.photographerId = :id")
    Long countByPhotographerId(@Param("id") Long id);
}