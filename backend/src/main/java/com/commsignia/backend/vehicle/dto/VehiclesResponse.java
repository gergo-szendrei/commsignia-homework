package com.commsignia.backend.vehicle.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehiclesResponse {

	private List<Vehicle> vehicles;
}
