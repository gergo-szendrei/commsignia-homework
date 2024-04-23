package com.commsignia.backend.vehicle.service;

import com.commsignia.backend.vehicle.dto.Position;
import com.commsignia.backend.vehicle.dto.RegisterResponse;
import com.commsignia.backend.vehicle.dto.VehiclesResponse;

public interface VehicleService {

	VehiclesResponse getVehiclesWithPosition();

	VehiclesResponse getVehiclesInRadius(Double latitude, Double longitude, Long radius);

	RegisterResponse registerVehicle();

	void updatePosition(Long id, Position position);
}
