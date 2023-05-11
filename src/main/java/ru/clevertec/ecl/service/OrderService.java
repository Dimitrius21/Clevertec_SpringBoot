package ru.clevertec.ecl.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Item;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.entity.dto.OrderDto;
import ru.clevertec.ecl.entity.dto.OrderForCreate;
import ru.clevertec.ecl.exception.RequestBodyIncorrectException;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.util.OrderMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс реализующий операции из слоя Сервис для Order
 */

@Service
@AllArgsConstructor
@Transactional
public class OrderService {
    private UserRepository userRepo;
    private OrderRepository orderRepo;
    private CertificateRepository certRepo;
    private OrderMapper mapper;

    /**
     * Метод формирующий сущность Order в полном объеме и полностью сохраняющий ее в базе данных
     * @param orderForCreate - данные для Order из Http-запроса
     * @return
     */
    public OrderDto createOrder(OrderForCreate orderForCreate) {
        Map<Long, Long> certQuantity = orderForCreate.getCerts().stream().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        Collection<Long> certificatesId = certQuantity.keySet();
        long userId = orderForCreate.getUserId();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RequestBodyIncorrectException("User with id = " + userId + "don't exist", 40004));
        List<GiftCertificate> certificates = certRepo.findAllById(certificatesId);
        List<Item> items = new ArrayList<>();
        Order order = new Order();
        long amount = 0;
        for (GiftCertificate cert : certificates) {
            long certId = cert.getId();
            long quantity = certQuantity.get(certId);
            Item item = new Item(0, (int) quantity, order, cert);
            items.add(item);
            amount += quantity * cert.getPrice();
        }
        order.setAmount(amount);
        order.setUser(user);
        order.setItems(items);
        order.setCreateTime(LocalDateTime.now());
        order = orderRepo.save(order);
        return mapper.orderDto(order);
    }
}
