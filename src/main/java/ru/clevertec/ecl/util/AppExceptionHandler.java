package ru.clevertec.ecl.util;

import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.ecl.entity.ErrorIfo;
import ru.clevertec.ecl.exception.NotValidRequestParametersException;
import ru.clevertec.ecl.exception.RequestBodyIncorrectException;
import ru.clevertec.ecl.exception.ResourceNotFountException;

/**
 * Класс для обработки возниуающих в приложении исключений
 */

@RestControllerAdvice
/**
 * Метод реализующий обработку возникших в приложении исключений
 */
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFountException.class)
    protected ResponseEntity<Object> handleEntityNotFoundEx(ResourceNotFountException ex, WebRequest request) {
        ErrorIfo error = new ErrorIfo("Entity Not Found " + ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotValidRequestParametersException.class, RequestBodyIncorrectException.class})
    protected ResponseEntity<Object> handleNotValidRequestData(NotValidRequestParametersException ex, WebRequest request) {
        ErrorIfo error = new ErrorIfo(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorIfo error = new ErrorIfo("Couldn't read/parse request body " + ex.getMessage(), 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
