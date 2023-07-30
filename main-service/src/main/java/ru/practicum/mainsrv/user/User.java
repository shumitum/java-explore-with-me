package ru.practicum.mainsrv.user;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(name = "name")
    String name;

    @NotBlank
    @Column(name = "email")
    String email;
}