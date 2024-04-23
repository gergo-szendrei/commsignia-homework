package com.commsignia.backend.notification.repository;

import com.commsignia.backend.notification.entity.NotificationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {

}
