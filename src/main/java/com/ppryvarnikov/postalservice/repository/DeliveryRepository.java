package com.ppryvarnikov.postalservice.repository;

import com.ppryvarnikov.postalservice.model.Delivery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    @Modifying
    @Query(value = "update deliveries set " +
            "id_parcel_status = :parcelStatusId, " +
            "date_time_status_change = :dateTimeStatusChange " +
            "where id = :deliveryId", nativeQuery = true)
    int update(@Param("deliveryId") Long deliveryId,
                @Param("parcelStatusId") Short parcelStatusId,
                @Param("dateTimeStatusChange") LocalDateTime dateTimeStatusChange);

    @Query(value = "select * " +
            "from deliveries d " +
            "left join parcel_statuses ps " +
            "on d.id_parcel_status = ps.id " +
            "where ps.status in ('Taken', 'Overdue')", nativeQuery = true)
    List<Delivery> findProcessed();

    @Query(value = "select * " +
            "from deliveries d " +
            "left join parcel_statuses ps " +
            "on d.id_parcel_status = ps.id " +
            "where ps.status = 'New'", nativeQuery = true)
    Collection<Delivery> findUnprocessed();
}