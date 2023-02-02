package com.lahutina.model;

public enum RoleData {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    private final String name;

    RoleData(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
