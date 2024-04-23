package com.commsignia.backend.notification.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationsResponse {

	private List<Notification> notifications;

	private Long totalCount;
}
