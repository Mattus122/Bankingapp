package com.example.bankingapp.repository;

import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID>,UserRepositoryCustom{
    Optional<User> findByEmail(String email);
    Optional<List<User>> findByOrganisationName(OrganisationName organisationName);
    @Query("SELECT u.organisationName FROM User u WHERE u.email = :email")
    List<OrganisationName> findOrganisationNamesByEmail(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.creationTimeStamp BETWEEN :startDate AND :endDate AND u.organisationName = :organisationName")
    List<User> findUsersByCreationTimeAndOrganisation(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("organisationName") OrganisationName organisationName);
    @Query("SELECT u FROM User u WHERE u.creationTimeStamp BETWEEN :startDate AND :endDate")
    List<User> findUsersByCreationTimeStamp(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<User> findByOrganisationNameAndRole(OrganisationName organisationName,Role role);


}
