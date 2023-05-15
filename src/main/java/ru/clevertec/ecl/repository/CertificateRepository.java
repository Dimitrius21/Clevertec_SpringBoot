package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.entity.GiftCertificate;
import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<GiftCertificate, Long> {
    final String requestFindByTagName = "select distinct cert.* from gift_certificate cert " +
            "where cert.id IN (select certificate_id from tag t " +
            "inner join certificate_tag ct " +
            "on t.id=ct.tag_id " +
            "where t.name= ?1 )";

    @Query(nativeQuery = true, value = requestFindByTagName)
    public List<GiftCertificate> findByTagName(String name, Pageable pageable);

}