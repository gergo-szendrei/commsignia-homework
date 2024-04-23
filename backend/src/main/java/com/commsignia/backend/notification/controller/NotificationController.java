package com.commsignia.backend.notification.controller;

import com.commsignia.backend.notification.dto.NewNotification;
import com.commsignia.backend.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class NotificationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@PostMapping("notifications")
	public ResponseEntity<Void> createNotification(
			@RequestBody NewNotification newNotification) {
		LOGGER.info("Create notification controller called with {}", newNotification);
		notificationService.createNotification(newNotification);
		return ResponseEntity.ok().build();
	}
}
