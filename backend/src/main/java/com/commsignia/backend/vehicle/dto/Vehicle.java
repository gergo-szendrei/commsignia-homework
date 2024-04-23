package com.commsignia.backend.vehicle.dto;

import com.commsignia.backend.vehicle.entity.VehicleEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle {

	private Long id;

	private Double latitude;

	private Double longitude;

	public static Vehicle toVehicle(VehicleEntity vehicleEntity) {
		return Vehicle.builder()
				.id(vehicleEntity.getId())
				.latitude(vehicleEntity.getLatitude())
				.longitude(vehicleEntity.getLongitude())
				.build();
	}
}
