package com.app.boot_app.domain.auth.repository;

import com.app.boot_app.domain.auth.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
    List<Group> findByEnterpriseIdAndIsDeletedFalse(UUID enterpriseId);

    Optional<Group> findByIdAndEnterpriseId(UUID groupId, UUID enterpriseId);
}
