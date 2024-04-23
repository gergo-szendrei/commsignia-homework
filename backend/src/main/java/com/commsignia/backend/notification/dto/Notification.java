package com.commsignia.backend.notification.dto;

import com.commsignia.backend.notification.entity.NotificationEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {

	private Long id;

	private Long vehicleId;

	private String message;

	public static Notification toNotification(NotificationEntity notificationEntity) {
		return Notification.builder()
				.id(notificationEntity.getId())
				.vehicleId(notificationEntity.getVehicleId())
				.message(notificationEntity.getMessage())
				.build();
	}
}
