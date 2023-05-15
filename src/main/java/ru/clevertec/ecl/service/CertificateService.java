package ru.clevertec.ecl.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.ResourceNotFountException;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс реализующий операции из слоя Сервис для GiftCertificate
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
public class CertificateService {

    @Autowired
    CertificateRepository certRepo;
    @Autowired
    TagRepository tagRepo;

    /**
     * Подготовка и сохранение в базе данных полученного в http-запросе GiftCertificate
     * @param certInRequest объект GiftCertificate извлеченный из тела запроса на сохранение
     * @return GiftCertificate сохраненный в DB c id
     */
    public GiftCertificate createCertificate(GiftCertificate certInRequest) {
        GiftCertificate certificate = certInRequest;
        List<Tag> tags = certificate.getTags();
        LocalDateTime now = LocalDateTime.now();
        certificate.setLastUpdateDate(now);
        certificate.setCreateDate(ZonedDateTime.of(now, ZoneId.systemDefault()));
        List<Tag> tagsForSave = tags.stream().filter(t -> t.getId() < 1).toList();
        tagRepo.saveAll(tagsForSave);
        certRepo.save(certificate);
        return certificate;
    }

    /**
     * Подготовка и обновление в базе данных полученного в http-запросе GiftCertificate
     * @param certInRequest объект GiftCertificate извлеченный из тела запроса на обновление
     * @return GiftCertificate сохраненный в DB
     */
    public GiftCertificate updateCertAllFields(GiftCertificate certInRequest) {

        long certId = certInRequest.getId();
        GiftCertificate certInDB = certRepo.findById(certId)
                .orElseThrow(()->new ResourceNotFountException(" id = " + certId, 40402));
        if (certInRequest.getName() != null && !certInRequest.getName().isEmpty()) {
            certInDB.setName(certInRequest.getName());
        }
        if (certInRequest.getDescription() != null && !certInRequest.getDescription().isEmpty()) {
            certInDB.setDescription(certInRequest.getDescription());
        }
        if (certInRequest.getPrice() > 0) {
            certInDB.setPrice(certInRequest.getPrice());
        }
        if (certInRequest.getDuration() > 0) {
            certInDB.setDuration(certInRequest.getDuration());
        }
        certInDB.setLastUpdateDate(LocalDateTime.now());
        List<Tag> tagsInRequest = certInRequest.getTags();
        List<Tag> tagsInDb = certInDB.getTags();
        //обновляем тэги GiftCertificate из DB на имещиеся в запросе
        for (Tag tag : tagsInRequest) {
            long id = tag.getId();
            Optional<Tag> tagInDb = tagsInDb.stream().filter(t -> t.getId() == id).findFirst();
            if (tagInDb.isPresent()) {
                updateTagAllField(tagInDb.get(), tag);
            } else {
                certInDB.addTag(tag);
            }
        }
        certInDB = certRepo.save(certInDB); //!!!!
        return certInDB;
    }

    /**
     * метод обновляет Тэги GiftCertificate извлеченнгые из DB на пришедшие в запросе на обновление
     * @param tagInDB - Тэги GiftCertificate прочитанного из DB
     * @param tagInRequest - Тэги GiftCertificate из пришедшего запроса
     */
    private void updateTagAllField(Tag tagInDB, Tag tagInRequest) {
        if (!Objects.isNull(tagInRequest.getName()) || !"".equals(tagInRequest.getName())) {
            tagInDB.setName(tagInRequest.getName());
        }
    }
}
