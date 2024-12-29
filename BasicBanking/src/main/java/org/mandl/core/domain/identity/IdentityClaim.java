package org.mandl.core.domain.identity;

import org.mandl.core.domain.entities.base.BaseEntity;

import java.util.UUID;

public class IdentityClaim extends BaseEntity {
    private final String type;
    private final String value;

    private IdentityClaim(UUID id, String type, final String value) {
        this.type = type;
        this.value = value;
        this.setId(id);
    }

    public static IdentityClaim create(final String type, final Object value) {
        return new IdentityClaim(UUID.randomUUID(), type, String.valueOf(value));
    }

    public static IdentityClaim create(UUID id, final String type, final Object value) {
        return new IdentityClaim(id, type, String.valueOf(value));
    }

    ///  GETTER & SETTER

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
