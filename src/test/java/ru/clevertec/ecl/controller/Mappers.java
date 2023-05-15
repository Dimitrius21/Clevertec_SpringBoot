package ru.clevertec.ecl.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.clevertec.ecl.util.OrderMapper;
import ru.clevertec.ecl.util.UserMapper;

@Configuration
public class Mappers {
    @Bean
    public UserMapper userMapper(){
        return org.mapstruct.factory.Mappers.getMapper(UserMapper.class);
    }

    @Bean
    @Primary
    public OrderMapper orderMapper(){
        return org.mapstruct.factory.Mappers.getMapper(OrderMapper.class);
    }


}
