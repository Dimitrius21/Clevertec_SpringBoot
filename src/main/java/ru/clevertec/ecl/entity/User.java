package ru.clevertec.ecl.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String password;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
