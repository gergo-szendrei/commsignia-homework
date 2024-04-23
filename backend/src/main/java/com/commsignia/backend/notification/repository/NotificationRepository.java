package com.commsignia.backend.notification.repository;

import com.commsignia.backend.notification.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository
		extends CrudRepository<NotificationEntity, Long>, PagingAndSortingRepository<NotificationEntity, Long> {

	Page<NotificationEntity> findAllByVehicleId(Long vehicleId, Pageable pageable);
}
