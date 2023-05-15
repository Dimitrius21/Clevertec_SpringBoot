package ru.clevertec.ecl.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForCreate {
    private long userId;
    private List<Long> certs;
}
