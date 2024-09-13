package com.ecommerce.website.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    @JsonValue
    public String getValue() {
        return role;
    }

    @JsonCreator
    public static UserRole fromValue(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.role.equalsIgnoreCase(value)) {  // Handles case insensitivity
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}
