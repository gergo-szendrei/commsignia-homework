package com.commsignia.backend.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

	private static final Long VEHICLE_ID_A = 101L;

	private static final Long NOTIFICATION_ID_A = 1001L;

	private static final Long NOTIFICATION_ID_B = 1002L;

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private VehicleRepository vehicleRepository;

	private NotificationService notificationService;

	@BeforeEach
	void setup() {
		notificationService = new NotificationServiceImpl(notificationRepository, vehicleRepository);
	}

	@Test
	void getNotificationsForVehicleShouldCallRepositoryWithCorrectParameters() {
		Pageable pageable = PageRequest.of(0, 50);

		Page<NotificationEntity> page = new PageImpl<>(List.of());
		doReturn(page).when(notificationRepository).findAllByVehicleId(VEHICLE_ID_A, pageable);

		notificationService.getNotificationsForVehicle(VEHICLE_ID_A, 0, 50);
		verify(notificationRepository, times(1)).findAllByVehicleId(VEHICLE_ID_A, pageable);
	}

	@Test
	void getNotificationsForVehicleShouldMapSingleDataCorrectlyToVehicle() {
		Pageable pageable = PageRequest.of(0, 50);

		NotificationEntity notificationEntityA = getNotificationEntity(NOTIFICATION_ID_A, VEHICLE_ID_A, "A");
		List<NotificationEntity> notificationEntities = List.of(notificationEntityA);
		Page<NotificationEntity> page = new PageImpl<>(notificationEntities);
		doReturn(page).when(notificationRepository).findAllByVehicleId(VEHICLE_ID_A, pageable);

		NotificationsResponse notificationsResponse = notificationService.getNotificationsForVehicle(VEHICLE_ID_A, 0, 50);
		assertThat(notificationsResponse.getNotifications()).hasSize(1);
		assertThat(notificationsResponse.getTotalCount()).isEqualTo(1);

		Notification notificationA = notificationsResponse.getNotifications().get(0);
		assertThat(notificationA.getId()).isEqualTo(NOTIFICATION_ID_A);
		assertThat(notificationA.getVehicleId()).isEqualTo(VEHICLE_ID_A);
		assertThat(notificationA.getMessage()).isEqualTo("A");
	}

	@Test
	void getNotificationsForVehicleShouldMapMultipleDataCorrectlyToVehicle() {
		Pageable pageable = PageRequest.of(0, 50);

		NotificationEntity notificationEntityA = getNotificationEntity(NOTIFICATION_ID_A, VEHICLE_ID_A, "A");
		NotificationEntity notificationEntityB = getNotificationEntity(NOTIFICATION_ID_B, VEHICLE_ID_A, "B");
		List<NotificationEntity> notificationEntities = List.of(notificationEntityA, notificationEntityB);
		Page<NotificationEntity> page = new PageImpl<>(notificationEntities);
		doReturn(page).when(notificationRepository).findAllByVehicleId(VEHICLE_ID_A, pageable);

		NotificationsResponse notificationsResponse = notificationService.getNotificationsForVehicle(VEHICLE_ID_A, 0, 50);
		assertThat(notificationsResponse.getNotifications()).hasSize(2);
		assertThat(notificationsResponse.getTotalCount()).isEqualTo(2);

		Notification notificationA = notificationsResponse.getNotifications().get(0);
		assertThat(notificationA.getId()).isEqualTo(NOTIFICATION_ID_A);
		assertThat(notificationA.getVehicleId()).isEqualTo(VEHICLE_ID_A);
		assertThat(notificationA.getMessage()).isEqualTo("A");

		Notification notificationB = notificationsResponse.getNotifications().get(1);
		assertThat(notificationB.getId()).isEqualTo(NOTIFICATION_ID_B);
		assertThat(notificationB.getVehicleId()).isEqualTo(VEHICLE_ID_A);
		assertThat(notificationB.getMessage()).isEqualTo("B");
	}

	@Test
	void createNotificationShouldCallRepositoryWithCorrectParameters() {
		NewNotification newNotification = NewNotification.builder()
				.vehicleId(VEHICLE_ID_A)
				.message("message")
				.build();
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setVehicleId(VEHICLE_ID_A);
		notificationEntity.setMessage("message");

		VehicleEntity vehicleEntityA = new VehicleEntity();
		vehicleEntityA.setId(VEHICLE_ID_A);
		vehicleEntityA.setLatitude(11D);
		vehicleEntityA.setLongitude(12D);
		doReturn(Optional.of(vehicleEntityA)).when(vehicleRepository).findById(VEHICLE_ID_A);

		notificationService.createNotification(newNotification);
		verify(vehicleRepository, times(1)).findById(VEHICLE_ID_A);
		verify(notificationRepository, times(1)).save(notificationEntity);
	}

	@Test
	void createNotificationShouldThrowExceptionIfVehicleNotFound() {
		NewNotification newNotification = NewNotification.builder()
				.vehicleId(VEHICLE_ID_A)
				.message("message")
				.build();

		CustomRuntimeException e = assertThrows(CustomRuntimeException.class,
				() -> notificationService.createNotification(newNotification));
		assertThat(e.getMessage()).isEqualTo(
				"Cannot update position. Vehicle by id " + VEHICLE_ID_A + " cannot be found.");
	}

	private NotificationEntity getNotificationEntity(Long id, Long vehicleId, String message) {
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setId(id);
		notificationEntity.setVehicleId(vehicleId);
		notificationEntity.setMessage(message);
		return notificationEntity;
	}
}