package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class RequestBodyIncorrectException extends RuntimeException{
    private long errorCode;

    public RequestBodyIncorrectException(String message, long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RequestBodyIncorrectException(String message) {
        super(message);
    }
}


