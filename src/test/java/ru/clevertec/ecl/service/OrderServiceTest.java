package ru.clevertec.ecl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.ecl.entity.*;
import ru.clevertec.ecl.entity.dto.OrderDto;
import ru.clevertec.ecl.entity.dto.OrderForCreate;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.util.OrderMapper;
import java.util.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private UserRepository userRepo;
    @Mock
    private OrderRepository orderRepo;
    @Mock
    private CertificateRepository certRepo;
    @Autowired
    @Spy
    private OrderMapper mapper;
    @InjectMocks
    private OrderService orderService;

    @Test
    public void createOrderTest(){
        long userId = 1L;
        User user = new User(userId, "user", "123", "user@mail.com", null);
        doReturn(Optional.of(user)).when(userRepo).findById(userId);

        GiftCertificate cert2 = certForTest(2L, "Quad bike", "Road 10km, 2 person", 9500 );
        GiftCertificate cert4 = certForTest(4L, "Rest1", "sauna", 1900);
        List<GiftCertificate> certificates = Arrays.asList(cert2, cert4);
        Collection<Long> certificatesId = new HashSet<>(Arrays.asList(2L, 4L));
        doReturn(certificates).when(certRepo).findAllById(certificatesId);

        Order orderSaved = new Order(1, null, 13300, user, null);
        when(orderRepo.save(argThat(o->o.getId()==0 && o.getAmount()==13300 && o.getItems().size()==2))).thenReturn(orderSaved);

        List<Long> certIds = Arrays.asList(2L, 4L,4L);
        OrderForCreate inOrder = new OrderForCreate(userId, certIds);
        OrderDto res = orderService.createOrder(inOrder);

        OrderDto expectedOrder = new OrderDto(1, null, 13300);
        Assertions.assertThat(res).isEqualTo(expectedOrder);

    }
    private GiftCertificate certForTest(long id, String name, String description, int price){
        GiftCertificate cert = new GiftCertificate(id, name, description, price, 40, null, null);
        Tag tag1 = new Tag(4, "motor");
        Tag tag2 = new Tag(0, "together");
        cert.addTag(tag1);
        cert.addTag(tag2);
        return cert;
    }
}