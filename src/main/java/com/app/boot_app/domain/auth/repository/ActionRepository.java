package com.app.boot_app.domain.auth.repository;

import com.app.boot_app.domain.auth.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActionRepository extends JpaRepository<Action, UUID> {
}
