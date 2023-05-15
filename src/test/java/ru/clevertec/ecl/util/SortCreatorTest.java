package ru.clevertec.ecl.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.exception.NotValidRequestParametersException;

import java.lang.reflect.MalformedParametersException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SortCreatorTest {

    @Test
    void getSpringSortWithoutParameterTest() {
        Optional<List<String>> sortParams = Optional.empty();
        Map<String, String> accordance = new HashMap<>();
        Sort sort = SortCreator.getSpringSort(sortParams, accordance);
        Assertions.assertThat(sort.isSorted()).isFalse();
    }

    @Test
    void getSpringSortForCertificateTest() {
        List<String> params = Arrays.asList("name-asc", "date-desc");
        Optional<List<String>> sortParams = Optional.of(params);
        Map<String, String> accordance = Map.of("name", "name", "date", "createDate");
        Sort sort = SortCreator.getSpringSort(sortParams, accordance);
        Assertions.assertThat(sort.getOrderFor("name").isAscending()).isTrue();
        Assertions.assertThat(sort.getOrderFor("createDate").isDescending()).isTrue();
    }

    @Test
    void getSpringSortInvalidParameterTest() {
        List<String> params = Arrays.asList("name-asc", "createDate-desc");
        Optional<List<String>> sortParams = Optional.of(params);
        Map<String, String> accordance = Map.of("name", "name", "date", "createDate");
        Assertions.assertThatThrownBy(() -> {
            SortCreator.getSpringSort(sortParams, accordance);
        }).isInstanceOf(NotValidRequestParametersException.class).hasMessageContaining("Parameter createDate is not valid");
    }
}