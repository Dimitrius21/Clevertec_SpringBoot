package ru.clevertec.ecl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.entity.User;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@Testcontainers
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

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
    public void testGetOne() {
        Optional<User> user = userRepository.findById(1L);
        Assertions.assertThat(user.isPresent()).isTrue();
        Assertions.assertThat(user.get().getUserName()).isEqualTo("Elen");
    }

    @Test
    public void testGetByName() {
        Optional<User> user = userRepository.findByUserName("Sema");
        Assertions.assertThat(user.isPresent()).isTrue();
        Assertions.assertThat(user.get().getEmail()).isEqualTo("sema@mail.com");
    }
    @Test
    public void testGetAll() {
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users).hasSize(3);
    }
}