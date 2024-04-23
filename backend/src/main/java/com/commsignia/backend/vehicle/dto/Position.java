package com.commsignia.backend.vehicle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Position {

	@NotNull
	private Double latitude;

	@NotNull
	private Double longitude;
}
