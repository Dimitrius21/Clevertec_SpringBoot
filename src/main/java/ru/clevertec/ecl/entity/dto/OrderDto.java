package ru.clevertec.ecl.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "createTime")
public class OrderDto {
    private long id;
    private LocalDateTime createTime;
    private long amount;
}
