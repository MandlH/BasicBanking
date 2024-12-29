package org.mandl.core.domain.identity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

public class IdentityClaim {

    @Id
    @GeneratedValue
    private UUID id;

    private final String type;
    private final String value;

    private IdentityClaim(UUID id, String type, final String value) {
        this.type = type;
        this.value = value;
        this.id = id;
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
