package com.example.sosoi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sosoi.entity.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
  
}
