package com.commsignia.backend.notification.service;

import java.util.Optional;

import com.commsignia.backend.notification.dto.NewNotification;
import com.commsignia.backend.notification.entity.NotificationEntity;
import com.commsignia.backend.notification.repository.NotificationRepository;
import com.commsignia.backend.shared.CustomRuntimeException;
import com.commsignia.backend.vehicle.entity.VehicleEntity;
import com.commsignia.backend.vehicle.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// Can rely on auto-created transaction and omit the annotation, but it is a good practice to follow the annotation approach
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

	private final NotificationRepository notificationRepository;

	private final VehicleRepository vehicleRepository;

	public NotificationServiceImpl(NotificationRepository notificationRepository, VehicleRepository vehicleRepository) {
		this.notificationRepository = notificationRepository;
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public void createNotification(NewNotification newNotification) {
		LOGGER.info("Create notification service called with {}", newNotification);
		Optional<VehicleEntity> optional = vehicleRepository.findById(newNotification.getVehicleId());
		if (optional.isEmpty()) {
			throw new CustomRuntimeException(String.format("Cannot update position. Vehicle by id %1$s cannot be found.",
					newNotification.getVehicleId()));
		}
		NotificationEntity notificationEntity = new NotificationEntity(
				newNotification.getVehicleId(),
				newNotification.getMessage());

		LOGGER.info("Saving notification: {}", notificationEntity);
		notificationRepository.save(notificationEntity);
	}
}
