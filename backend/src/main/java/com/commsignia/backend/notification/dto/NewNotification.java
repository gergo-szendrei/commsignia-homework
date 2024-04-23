package com.commsignia.backend.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewNotification {

	@NotNull
	@JsonProperty("vehicle_id")
	private Long vehicleId;

	@NotNull
	private String message;
}
