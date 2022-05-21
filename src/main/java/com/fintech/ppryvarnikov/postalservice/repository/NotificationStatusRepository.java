package com.fintech.ppryvarnikov.postalservice.repository;

import com.fintech.ppryvarnikov.postalservice.model.NotificationStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStatusRepository extends CrudRepository<NotificationStatus, Short> {
    @Modifying
    @Query(value = "update notification_statuses set " +
            "status = :status " +
            "where id = :notificationStatusId", nativeQuery = true)
    int update(@Param("notificationStatusId") Short notificationStatusId,
               @Param("status") String status);
}