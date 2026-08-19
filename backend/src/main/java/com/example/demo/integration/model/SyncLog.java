package com.example.demo.integration.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sync_log")
@Data
@NoArgsConstructor
public class SyncLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    private String status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}