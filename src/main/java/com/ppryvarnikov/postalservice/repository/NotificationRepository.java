package com.ppryvarnikov.postalservice.repository;

import com.ppryvarnikov.postalservice.model.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    @Modifying
    @Query(value = "update notifications set " +
            "message = :message, " +
            "id_status = :notificationStatusId " +
            "where id = :notificationId", nativeQuery = true)
    int update(@Param("notificationId") Long notificationId,
                @Param("message") String message,
                @Param("notificationStatusId") Short notificationStatusId);

    @Query(value = "select * " +
            "from notifications n " +
            "left join notification_statuses ns " +
            "on n.id_status = ns.id " +
            "where ns.status = 'New'", nativeQuery = true)
    Collection<Notification> findUnprocessed();
}