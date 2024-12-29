package org.mandl.identity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table
public class IdentityClaim {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String value;

    public IdentityClaim() {
    }

    private IdentityClaim(UUID id, String type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public static IdentityClaim create(String type, Object value) {
        return new IdentityClaim(UUID.randomUUID(), type, String.valueOf(value));
    }

    public static IdentityClaim create(UUID id, String type, Object value) {
        return new IdentityClaim(id, type, String.valueOf(value));
    }
}
