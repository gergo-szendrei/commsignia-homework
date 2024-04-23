package com.commsignia.backend.notification.controller;

import com.commsignia.backend.notification.dto.NewNotification;
import com.commsignia.backend.notification.dto.NotificationsResponse;
import com.commsignia.backend.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping
	// This is only valid for this demo app, in prod origins must be set properly
	@CrossOrigin(origins = "*")
	public ResponseEntity<NotificationsResponse> getNotificationsForVehicle(
			@RequestParam(value = "vehicleId") Long vehicleId,
			@RequestParam(value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize) {
		LOGGER.info("Get notifications for vehicle controller called with vehicleId {}, pageNumber {}, pageSize {}",
				vehicleId, pageNumber, pageSize);
		return ResponseEntity.ok(notificationService.getNotificationsForVehicle(vehicleId, pageNumber, pageSize));
	}

	@PostMapping
	public ResponseEntity<Void> createNotification(
			@RequestBody NewNotification newNotification) {
		LOGGER.info("Create notification controller called with {}", newNotification);
		notificationService.createNotification(newNotification);
		return ResponseEntity.ok().build();
	}
}
