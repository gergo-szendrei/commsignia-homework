package com.commsignia.backend.notification.service;

import java.util.List;
import java.util.Optional;

import com.commsignia.backend.notification.dto.NewNotification;
import com.commsignia.backend.notification.dto.Notification;
import com.commsignia.backend.notification.dto.NotificationsResponse;
import com.commsignia.backend.notification.entity.NotificationEntity;
import com.commsignia.backend.notification.repository.NotificationRepository;
import com.commsignia.backend.shared.CustomRuntimeException;
import com.commsignia.backend.vehicle.entity.VehicleEntity;
import com.commsignia.backend.vehicle.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	public NotificationsResponse getNotificationsForVehicle(Long vehicleId, Integer pageNumber, Integer pageSize) {
		LOGGER.info("Get notifications for vehicle service called with vehicleId {}, pageNumber {}, pageSize {}",
				vehicleId, pageNumber, pageSize);
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<NotificationEntity> page = notificationRepository.findAllByVehicleId(vehicleId, pageable);
		List<Notification> notifications = page.getContent().stream()
				.map(Notification::toNotification)
				.toList();
		return NotificationsResponse.builder()
				.notifications(notifications)
				.totalCount(page.getTotalElements())
				.build();
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
