package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.entity.dto.OrderDto;
import ru.clevertec.ecl.entity.dto.OrderForCreate;
import ru.clevertec.ecl.service.OrderService;

/**
 * Класс реализующий слой контроллера для запросов по Order
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderForCreate order){
       return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }
}

