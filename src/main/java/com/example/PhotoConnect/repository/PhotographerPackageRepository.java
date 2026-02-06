package com.example.PhotoConnect.repository;

import com.example.PhotoConnect.model.PhotographerPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotographerPackageRepository extends JpaRepository<PhotographerPackage, Long> {
    List<PhotographerPackage> findByPhotographerId(Long photographerId);
}

