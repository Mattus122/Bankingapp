package com.example.bankingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "CUSTOM_USER" ,
uniqueConstraints = {
        @UniqueConstraint(name = "email_unique" , columnNames = "email")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String email;
    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER , orphanRemoval = true)
    private List<Account> accounts;




}
