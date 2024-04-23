package com.commsignia.backend.notification.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.commsignia.backend.notification.dto.NewNotification;
import com.commsignia.backend.notification.dto.Notification;
import com.commsignia.backend.notification.dto.NotificationsResponse;
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

	private static final Long NOTIFICATION_ID_A = 1001L;

	private static final Long NOTIFICATION_ID_B = 1002L;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private NotificationService notificationService;

	@Test
	void getNotificationsForVehicleShouldPassCorrectParametersAndReturnCorrectData() throws Exception {
		Notification notificationA = Notification.builder().id(NOTIFICATION_ID_A).vehicleId(VEHICLE_ID_A).message("A").build();
		Notification notificationB = Notification.builder().id(NOTIFICATION_ID_B).vehicleId(VEHICLE_ID_A).message("B").build();
		List<Notification> notifications = List.of(notificationA, notificationB);
		NotificationsResponse notificationsResponse = NotificationsResponse.builder()
				.notifications(notifications)
				.totalCount(2L)
				.build();
		doReturn(notificationsResponse).when(notificationService).getNotificationsForVehicle(VEHICLE_ID_A, 0, 50);

		mvc.perform(MockMvcRequestBuilders
						.get("/notifications")
						.param("vehicleId", "101")
						.param("pageNumber", "0")
						.param("pageSize", "50"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.notifications.[0].id").value(NOTIFICATION_ID_A))
				.andExpect(jsonPath("$.notifications.[0].vehicleId").value(VEHICLE_ID_A))
				.andExpect(jsonPath("$.notifications.[0].message").value("A"))
				.andExpect(jsonPath("$.notifications.[1].id").value(NOTIFICATION_ID_B))
				.andExpect(jsonPath("$.notifications.[1].vehicleId").value(VEHICLE_ID_A))
				.andExpect(jsonPath("$.notifications.[1].message").value("B"))
				.andExpect(jsonPath("$.totalCount").value(2L));
		verify(notificationService, times(1)).getNotificationsForVehicle(VEHICLE_ID_A, 0, 50);
	}

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