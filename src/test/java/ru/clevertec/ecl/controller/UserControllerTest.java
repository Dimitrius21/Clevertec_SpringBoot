package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.entity.dto.UserDto;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.util.OrderMapper;
import ru.clevertec.ecl.util.SortCreator;
import ru.clevertec.ecl.util.UserMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
@Import(Mappers.class)
class UserControllerTest {
    private final static Map<String, String> ACCORDANCE = Map.of("name", "userName");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepo;
    @MockBean
    private OrderRepository orderRepo;
    @Autowired
    @SpyBean
    private UserMapper userMapper;
    @Autowired
    @SpyBean
    private OrderMapper orderMapper;

    @InjectMocks
    UserController userController;

    @Test
    void getAllUsers() throws Exception {
        User user1 = new User(1, "Jon", "123", "jon@mail.com", null);
        User user2  = new User(2, "Sema", "789", "sema@mail.com", null);
        Page<User> users = new PageImpl<>(Arrays.asList(user1, user2));
        Optional<List<String>> sortParams = Optional.of(Arrays.asList("name-asc"));
        Sort sort = SortCreator.getSpringSort(sortParams, ACCORDANCE);
        Pageable pageable = PageRequest.of(0, 5, sort);
        when(userRepo.findAll(pageable)).thenReturn(users);

        UserDto userDto1 = new UserDto(1, "Jon", "jon@mail.com");
        UserDto userDto2 = new UserDto(2, "Sema", "sema@mail.com");
        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        mockMvc.perform(
                        get("/user?page=0&size=5&sort=name-asc")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDtoList)));
    }
}