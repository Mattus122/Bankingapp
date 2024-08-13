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

//    @Column
//    private LocalDate dob;
//
//    @Column(name = "full_name")
//    private String fullName;

    @Column(name = "age")
    private Integer age;
    @Enumerated(EnumType.STRING)
    @Column
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER , orphanRemoval = true)
//    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL) in one to one  mapping mappedbyuser does not create  a fk of account id in user
    //table but a bidirectional relationshp is established so u can get user from accounts and account from user.
    //cascade all helps in performing operations on child table also if there are any changes in the main table .
    private List<Account> accounts;

//    @PrePersist
//    @PreUpdate
//    private void updateDerivedFields() {
//        this.fullName = getFullName();
//        this.age = getAge();
//    }
//
//    public String getFullName() {
//        return firstName + " " + lastName;
//    }
//
//    public Integer getAge() {
//        if (dob == null) {
//            return null;
//        }
//        return Period.between(dob, LocalDate.now()).getYears();
//    }


}
