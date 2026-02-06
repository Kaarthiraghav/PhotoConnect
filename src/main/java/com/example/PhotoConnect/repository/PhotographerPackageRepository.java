package com.example.PhotoConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PhotoConnect.model.PhotographerPackage;

import java.util.List;

public interface PhotographerPackageRepository extends JpaRepository<PhotographerPackage, Long> {
    List<PhotographerPackage> findByPhotographerId(Long photographerId);
}

