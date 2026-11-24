package com.example.sosoi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sosoi.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
  
}
