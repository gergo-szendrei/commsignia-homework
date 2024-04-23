package com.commsignia.backend.notification.service;

import com.commsignia.backend.notification.dto.NewNotification;

public interface NotificationService {

	void createNotification(NewNotification newNotification);
}
