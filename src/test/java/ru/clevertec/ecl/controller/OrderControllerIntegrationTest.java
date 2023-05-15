package ru.clevertec.ecl.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.entity.dto.OrderForCreate;
import org.hamcrest.core.IsNot;
import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createOrderTest() throws Exception {
        OrderForCreate order = new OrderForCreate(3, Arrays.asList(1L, 1L, 7L));
        Matcher<Long> matcherNotZero = IsNot.not(0L);
        Matcher<String> matcherContainDate = StringContains.containsString(LocalDate.now().toString());

        mockMvc.perform(
                        post("/order")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcherNotZero))
                .andExpect(jsonPath("$.amount").value(10800))
                .andExpect(jsonPath("$.createTime").value(matcherContainDate));
    }
}



