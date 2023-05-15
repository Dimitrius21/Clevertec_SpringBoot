package ru.clevertec.ecl.util;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    public OrderDto orderDto(Order order);
}
