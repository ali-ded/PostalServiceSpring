package com.fintech.ppryvarnikov.postalservice.repository;

import com.fintech.ppryvarnikov.postalservice.model.ParcelStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelStatusRepository extends CrudRepository<ParcelStatus, Short> {
    @Modifying
    @Query(value = "update parcel_statuses set " +
            "status = :status " +
            "where id = :parcelStatusId", nativeQuery = true)
    int update(@Param("parcelStatusId") Short parcelStatusId,
               @Param("status") String status);
}