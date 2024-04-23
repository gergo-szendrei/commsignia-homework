package com.commsignia.backend.notification.service;

import com.commsignia.backend.notification.dto.NewNotification;
import com.commsignia.backend.notification.dto.NotificationsResponse;

public interface NotificationService {

	NotificationsResponse getNotificationsForVehicle(Long vehicleId, Integer pageNumber, Integer pageSize);

	void createNotification(NewNotification newNotification);
}
