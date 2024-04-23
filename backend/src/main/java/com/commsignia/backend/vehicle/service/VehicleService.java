package com.commsignia.backend.vehicle.service;

import com.commsignia.backend.vehicle.dto.VehiclesResponse;

public interface VehicleService {

	VehiclesResponse getVehiclesInRadius(Double latitude, Double longitude, Long radius);
}
