package com.app.boot_app.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupInvitation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, UUID> {
    Optional<GroupInvitation> findByToken(String token);
    Optional<GroupInvitation> findByGroupAndEmailAndStatus(Group group, String email, GroupInvitation.InvitationStatus status);
}
