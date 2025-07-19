package com.app.boot_app.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "group_audit_logs")
public class GroupAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false, length = 50)
    private String action;

    @ManyToOne
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime performedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String oldValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String newValue;

    @Column(length = 45)
    private String ipAddress;

    public enum Action {
        USER_CREATED,
        USER_UPDATED,
        USER_DELETED,
        GROUP_CREATED,
        GROUP_UPDATED,
        GROUP_DELETED,
        MEMBER_ADDED,
        MEMBER_REMOVED,
        ROLE_ASSIGNED,
        INVITATION_SENT,
        INVITATION_ACCEPTED,
        INVITATION_REJECTED,
        INVITATION_CANCELLED
    }
}
