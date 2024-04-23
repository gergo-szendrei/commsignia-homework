package com.commsignia.backend.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.commsignia.backend.notification.dto.NewNotification;
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

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

	private static final Long VEHICLE_ID_A = 101L;

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
}