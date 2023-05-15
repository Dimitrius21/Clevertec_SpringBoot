package ru.clevertec.ecl.util;

import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.exception.NotValidRequestParametersException;

import java.util.*;

/**
 * Класс формирующий объект класса Sort фрэймворка Спрингдля сортировки получаемых из базы данных значений
 */

public class SortCreator {
    /**
     * Метод формирующий объект Спринга класса Sort
     * @param sortParams - строка с параметрами сортировки из Http-запроса
     * @param accordance - список соответсвий между полем для сортировки в запросе и именем этого поля в сущности
     * @return объект Спринга класса Sort
     */
    public static Sort getSpringSort(Optional<List<String>> sortParams, Map<String, String> accordance) {
        if (sortParams.isEmpty()) {
            return Sort.unsorted();
        }
        List<Sort.Order> sortFieldsList = sortParams.get().stream()
                .map(p -> p.split("-"))
                .map(s -> createSortOrder(s, accordance)
                ).toList();
        return Sort.by(sortFieldsList);
    }

    /**
     * Метод проверяет правильность параметра поле_сортировки-тип_сортировки из строки запроса и на их
     * основании формирует объект Sort.Order фрэймворка Спринг
     * @param params - входной параметр из запроса для одного поля
     * @param accordance - список соответсвий между полем для сортировки в запросе и именем этого поля в сущности
     * @return - объект Sort.Order фрэймворка Спринг
     */
    private static Sort.Order createSortOrder(String[] params, Map<String, String> accordance) {
        try {
            String field;
            if ((field = accordance.get(params[0])) == null) {
                throw new NotValidRequestParametersException("", 400);
            }
            return new Sort.Order(Sort.Direction.valueOf(params[1].trim().toUpperCase()), field);
        } catch (IllegalArgumentException ex) {
            throw new NotValidRequestParametersException("Value for Parameter " + params[1] + " is not valid", 400);
        }
    }
}
