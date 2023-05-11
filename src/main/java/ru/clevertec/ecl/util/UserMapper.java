package ru.clevertec.ecl.util;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.entity.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
}
