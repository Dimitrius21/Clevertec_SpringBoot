package ru.clevertec.ecl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {
    @Mock
    private TagRepository tagRepo;
    @Mock
    private CertificateRepository certRepo;
    @InjectMocks
    private CertificateService certService;

    @Test
    void createCertificateTest() {
        GiftCertificate cert = certForTest();
        Tag exp = cert.getTags().get(1);

        certService.createCertificate(cert);

        ArgumentCaptor<List<Tag>> captor = ArgumentCaptor.forClass(List.class);
        verify(tagRepo).saveAll(captor.capture());
        List<Tag> arg = captor.getValue();
        Assertions.assertThat(arg).isEqualTo(List.of(exp));
    }

    @Test
    void updateCertAllFieldsTest() {
        //Сертификат который будет в запросе от клиента
        GiftCertificate certInRequest = certForTest();
        certInRequest.setDescription("all inclusive");
        certInRequest.setPrice(0);
        certInRequest.setDuration(0);
        certInRequest.getTags().get(0).setName("relax!!!");

        //Сертификат который извлечется из базы при обращении внутри метода
        GiftCertificate cert = certForTest();
        cert.getTags().remove(1);
        doReturn(Optional.of(cert)).when(certRepo).findById(1L);

        //Сертификат который должен быть сформирован внутри метода и передан в Дао для обновления
        GiftCertificate certForUpdate = certForTest();
        certForUpdate.setDescription("all inclusive");
        certForUpdate.getTags().get(0).setName("relax!!!");

        certService.updateCertAllFields(certInRequest);

        ArgumentCaptor<GiftCertificate> captor = ArgumentCaptor.forClass(GiftCertificate.class);
        verify(certRepo).save(captor.capture());
        GiftCertificate arg = captor.getValue();
        Assertions.assertThat(arg).isEqualTo(certForUpdate);
    }

    private GiftCertificate certForTest(){
        GiftCertificate cert = new GiftCertificate(1, "Quad bike", "Road 10km, 2 person", 9500, 40, null, null);
        Tag tag1 = new Tag(4, "motor");
        Tag tag2 = new Tag(0, "together");
        cert.addTag(tag1);
        cert.addTag(tag2);
        return cert;
    }
}