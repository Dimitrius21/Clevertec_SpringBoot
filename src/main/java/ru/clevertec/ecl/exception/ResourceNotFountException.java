package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class ResourceNotFountException extends RuntimeException{
    public ResourceNotFountException(String message, Throwable cause) {
        super(message, cause);
    }
    private long errorCode;

    public ResourceNotFountException(String message) {
        super(message);
    }

    public ResourceNotFountException() {
    }

    public ResourceNotFountException(String message, long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
