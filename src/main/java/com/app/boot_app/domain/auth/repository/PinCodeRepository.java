package com.app.boot_app.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.boot_app.domain.auth.entity.PinCode;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PinCodeRepository extends JpaRepository<PinCode, UUID> {
    Optional<PinCode> findByUserIdAndCode(UUID userId, String code);
}
