package org.pos.retailpossystem.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String str) {
        super(str);
    }
}
