package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class NotValidRequestParametersException extends RuntimeException{
    private long errorCode;
    public NotValidRequestParametersException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotValidRequestParametersException(String message, long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotValidRequestParametersException(Throwable cause) {
        super(cause);
    }
}
