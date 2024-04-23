package com.commsignia.backend.vehicle.controller;

import com.commsignia.backend.vehicle.dto.Position;
import com.commsignia.backend.vehicle.dto.RegisterResponse;
import com.commsignia.backend.vehicle.dto.VehiclesResponse;
import com.commsignia.backend.vehicle.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleController.class);

	private final VehicleService vehicleService;

	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	@GetMapping("vehicles/all")
	// This is only valid for this demo app, in prod origins must be set properly
	@CrossOrigin(origins = "*")
	public ResponseEntity<VehiclesResponse> getVehiclesWithPosition() {
		LOGGER.info("Get vehicles with position controller called");
		return ResponseEntity.ok(vehicleService.getVehiclesWithPosition());
	}

	@GetMapping("vehicles")
	public ResponseEntity<VehiclesResponse> getVehiclesInRadius(
			@RequestParam(value = "latitude") Double latitude,
			@RequestParam(value = "longitude") Double longitude,
			@RequestParam(value = "radius") Long radius) {
		LOGGER.info("Get vehicles in radius controller called with latitude {}, longitude {}, radius {}",
				latitude, longitude, radius);
		return ResponseEntity.ok(vehicleService.getVehiclesInRadius(latitude, longitude, radius));
	}

	@PostMapping("vehicles")
	public ResponseEntity<RegisterResponse> registerVehicle() {
		LOGGER.info("Register vehicle controller called");
		return ResponseEntity.ok(vehicleService.registerVehicle());
	}

	@PostMapping("vehicle/{id}")
	public ResponseEntity<Void> updatePosition(
			@PathVariable("id") Long id,
			@RequestBody Position position) {
		LOGGER.info("Update position controller called with id {}, position {}", id, position);
		vehicleService.updatePosition(id, position);
		return ResponseEntity.ok().build();
	}
}
