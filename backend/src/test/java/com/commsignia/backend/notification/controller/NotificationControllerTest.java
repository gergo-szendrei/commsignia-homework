package com.commsignia.backend.notification.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.commsignia.backend.notification.dto.NewNotification;
import com.commsignia.backend.notification.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

	private static final Long VEHICLE_ID_A = 101L;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private NotificationService notificationService;

	@Test
	void createNotificationShouldPassCorrectParameters() throws Exception {
		NewNotification newNotification = NewNotification.builder()
				.vehicleId(VEHICLE_ID_A)
				.message("message")
				.build();

		mvc.perform(MockMvcRequestBuilders
						.post("/notifications")
						.content(toJsonString(newNotification))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(notificationService, times(1)).createNotification(newNotification);
	}

	private String toJsonString(NewNotification newNotification) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(newNotification);
	}
}