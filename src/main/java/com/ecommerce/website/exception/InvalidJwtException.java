package com.ecommerce.website.exception;

public class InvalidJwtException extends Exception {
    public InvalidJwtException(String message) {
        super(message);
    }
}