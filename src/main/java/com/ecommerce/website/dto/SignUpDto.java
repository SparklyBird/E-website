package com.ecommerce.website.dto;

import com.ecommerce.website.enums.UserRole;

public record SignUpDto(
        String login,
        String password,
        UserRole role
) {
}