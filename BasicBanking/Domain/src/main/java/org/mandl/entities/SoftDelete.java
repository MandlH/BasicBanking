package org.mandl.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Soft Delete Entities which are required for other entities
 */
public interface SoftDelete {
    void markAsDeleted();
    boolean isDeleted();
    void setDeletedAt(LocalDateTime deletedAt);
    LocalDateTime getDeletedAt();
}
