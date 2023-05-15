package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMapAdapter;
import ru.clevertec.ecl.entity.dto.OrderDto;
import ru.clevertec.ecl.entity.dto.UserDto;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.util.OrderMapper;
import ru.clevertec.ecl.util.UserMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("orderMapperImpl")
    private OrderMapper orderMapper;

    @Autowired
    @Qualifier("userMapperImpl")
    private UserMapper userMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getAllUsersTest() throws Exception {
        MultiValueMapAdapter params = new MultiValueMapAdapter<>(new HashMap<>());
        params.setAll(Map.of("page", "0", "size", "2"));

        mockMvc.perform(get("/user")
                        .queryParams(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].userName").value("Elen"))
                .andExpect(jsonPath("[1].userName").value("Jon"));
    }

    @Test
    public void findUserByIdTest() throws Exception {
        UserDto userDto = new UserDto(1, "Elen", "elen@mail.com");
        long id = userDto.getId();
        mockMvc.perform(
                        get("/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    public void findUserByNameTest() throws Exception {
        UserDto userDto = new UserDto(3, "Sema", "sema@mail.com");
        String userName = userDto.getUserName();
        mockMvc.perform(
                        get("/user/name/{userName}", userName))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    public void findUserOrdersTest() throws Exception {
        long id = 2;
        mockMvc.perform(
                        get("/user/{id}/orders", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("[0].amount").value(21000))
                .andExpect(jsonPath("[1].amount").value(21500))
                .andExpect(jsonPath("[2].amount").value(44000));
    }

    @Test
    public void findUserOrderTest() throws Exception {
        OrderDto orderDto = new OrderDto(2, LocalDateTime.of(2023, 05, 02, 0, 0), 21000);
        long orderId = orderDto.getId();
        long userId = 2;
        mockMvc.perform(
                        get("/user/{id}/order/{orderId}", userId, orderId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderDto)));
    }
}



