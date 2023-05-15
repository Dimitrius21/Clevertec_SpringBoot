package ru.clevertec.ecl.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;


@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(value = "SELECT * FROM tag WHERE name = ( " +
            "SELECT t.name FROM tag as t " +
            "inner join certificate_tag as ct " +
            "on t.id = ct.tag_id " +
            "inner join gift_certificate as c " +
            "on ct.certificate_id = c.id " +
            "inner join items as i " +
            "on i.cert_id = c.id " +
            "inner join orders as o " +
            "on i.order_id = o.id " +
            "where o.amount = (SELECT MAX(amount) FROM orders WHERE user_id = ?) " +
            "group by t.name " +
            "order by count(t.name) desc " +
            "limit 1)",
            nativeQuery = true)
    public Optional<Tag> findMostUsedTagMostExpensiveOrder(long userId);
}
