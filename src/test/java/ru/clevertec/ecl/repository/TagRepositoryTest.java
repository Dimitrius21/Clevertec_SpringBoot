package ru.clevertec.ecl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class TagRepositoryTest {

    @Autowired
    TagRepository tagRepository;
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2-alpine")
            .withUsername("user")
            .withPassword("psw")
            .withDatabaseName("testDb")
            .withInitScript("testData.sql");

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void findMostUsedTagMostExpensiveOrderTest() {
        Optional<Tag> tag = tagRepository.findMostUsedTagMostExpensiveOrder(2);
        Assertions.assertThat(tag.get()).extracting(Tag::getName).isEqualTo("relax");
    }
}