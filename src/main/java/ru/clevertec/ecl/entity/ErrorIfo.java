package ru.clevertec.ecl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс представляющий тео ответа в случае ошибки при обработке запроса
 */
@Data
@AllArgsConstructor
public class ErrorIfo {
    private String errorMessage;
    private long errorCode;
}
