package com.ppryvarnikov.postalservice.repository;

import com.ppryvarnikov.postalservice.model.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    @Modifying
    @Query(value = "update clients set " +
            "surname = :surname, " +
            "first_name = :firstName, " +
            "patronymic = :patronymic, " +
            "email = :email, " +
            "phone_number = :phoneNumber " +
            "where id = :clientId", nativeQuery = true)
    int update(@Param("clientId") Long clientId,
                @Param("surname") String surname,
                @Param("firstName") String firstName,
                @Param("patronymic") String patronymic,
                @Param("email") String email,
                @Param("phoneNumber") Long phoneNumber);

    Optional<Client> findByPhoneNumber(Long phoneNumber);
}