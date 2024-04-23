package com.commsignia.backend.vehicle.controller;

import com.commsignia.backend.vehicle.dto.Position;
import com.commsignia.backend.vehicle.dto.RegisterResponse;
import com.commsignia.backend.vehicle.dto.VehiclesResponse;
import com.commsignia.backend.vehicle.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class VehicleController {

	private final VehicleService vehicleService;

	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	@GetMapping("vehicles")
	public ResponseEntity<VehiclesResponse> getVehiclesInRadius(
			@RequestParam(value = "latitude") Double latitude,
			@RequestParam(value = "longitude") Double longitude,
			@RequestParam(value = "radius") Long radius) {
		return ResponseEntity.ok(vehicleService.getVehiclesInRadius(latitude, longitude, radius));
	}

	@PostMapping("vehicles")
	public ResponseEntity<RegisterResponse> registerVehicle() {
		return ResponseEntity.ok(vehicleService.registerVehicle());
	}

	@PostMapping("vehicle/{id}")
	public ResponseEntity<Void> updatePosition(
			@PathVariable("id") Long id,
			@RequestBody Position position) {
		vehicleService.updatePosition(id, position);
		return ResponseEntity.ok().build();
	}
}
