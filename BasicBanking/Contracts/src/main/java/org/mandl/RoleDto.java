package org.mandl;

import java.util.UUID;

public class RoleDto {
    private UUID id;
    private String name;

    public RoleDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
