package ru.clevertec.ecl.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.entity.dto.OrderDto;
import ru.clevertec.ecl.entity.dto.UserDto;
import ru.clevertec.ecl.exception.ResourceNotFountException;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.util.OrderMapper;
import ru.clevertec.ecl.util.SortCreator;
import ru.clevertec.ecl.util.UserMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс реализующий слой контроллера для запросов по User
 */

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private static final String ITEM_ON_PAGE = "10";
    private final static Map<String, String> ACCORDANCE = Map.of("name", "userName");

    private UserRepository userRepo;
    private OrderRepository orderRepo;
    private UserMapper userMapper;
    private OrderMapper orderMapper;


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam Optional<Integer> page,
                                                     @RequestParam(defaultValue = ITEM_ON_PAGE) int size,
                                                     @RequestParam(name = "sort") Optional<List<String>> sortParams) {
        Sort sort = SortCreator.getSpringSort(sortParams, ACCORDANCE);
        Pageable pageable = page.isPresent() ? PageRequest.of(page.get(), size, sort) : PageRequest.ofSize(Integer.MAX_VALUE).withSort(sort);
        List<User> users = userRepo.findAll(pageable).getContent();
        List<UserDto> response = users.stream().map(userMapper::toUserDto).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("id = " + id, 40403));
        UserDto response = userMapper.toUserDto(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/name/{userName}")
    public ResponseEntity<UserDto> findUserById(@PathVariable String userName) {
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFountException("name = " + userName, 40403));
        UserDto response = userMapper.toUserDto(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> findUserOrders(@PathVariable long id) {
        List<Order> response = orderRepo.findByUserId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/order/{orderId}")
    public ResponseEntity<OrderDto> findUserOrder(@PathVariable long id, @PathVariable long orderId) {
        Order order = orderRepo.findByIdAndUserId(orderId, id)
                .orElseThrow(() -> new ResourceNotFountException("user_id = " + id + " and orderId = " + orderId, 40403));
        OrderDto response = orderMapper.orderDto(order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
