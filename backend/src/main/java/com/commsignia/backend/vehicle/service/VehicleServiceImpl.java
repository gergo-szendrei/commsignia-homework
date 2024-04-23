package com.commsignia.backend.vehicle.service;

import java.util.List;
import java.util.Optional;

import com.commsignia.backend.shared.CustomRuntimeException;
import com.commsignia.backend.vehicle.dto.Position;
import com.commsignia.backend.vehicle.dto.RegisterResponse;
import com.commsignia.backend.vehicle.dto.Vehicle;
import com.commsignia.backend.vehicle.dto.VehiclesResponse;
import com.commsignia.backend.vehicle.entity.VehicleEntity;
import com.commsignia.backend.vehicle.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// Can rely on auto-created transaction and omit the annotation, but it is a good practice to follow the annotation approach
@Transactional
public class VehicleServiceImpl implements VehicleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleServiceImpl.class);

	private final VehicleRepository vehicleRepository;

	public VehicleServiceImpl(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public VehiclesResponse getVehiclesInRadius(Double latitude, Double longitude, Long radius) {
		LOGGER.info("Get vehicles in radius service called with latitude {}, longitude {}, radius {}",
				latitude, longitude, radius);
		List<Vehicle> vehicles = vehicleRepository.getVehiclesInRadius(latitude, longitude, radius).stream()
				.map(Vehicle::toVehicle)
				.toList();
		return VehiclesResponse.builder().vehicles(vehicles).build();
	}

	@Override
	public RegisterResponse registerVehicle() {
		LOGGER.info("Register vehicle service called");
		VehicleEntity vehicleEntity = new VehicleEntity();

		LOGGER.info("Saving vehicle: {}", vehicleEntity);
		// Assigning it back does not needed for proper operation, but helps testability
		vehicleEntity = vehicleRepository.save(vehicleEntity);

		return RegisterResponse.builder()
				.id(vehicleEntity.getId())
				.build();
	}

	@Override
	public void updatePosition(Long id, Position position) {
		LOGGER.info("Update position service called with id {}, position {}", id, position);
		Optional<VehicleEntity> optional = vehicleRepository.findById(id);
		if (optional.isEmpty()) {
			throw new CustomRuntimeException(String.format("Cannot update position. Vehicle by id %1$s cannot be found.", id));
		}

		VehicleEntity vehicleEntity = optional.get();
		vehicleEntity.setLatitude(position.getLatitude());
		vehicleEntity.setLongitude(position.getLongitude());

		LOGGER.info("Saving position: {}", vehicleEntity);
		// It would save even without calling save explicitly, but this approach helps testability
		vehicleRepository.save(vehicleEntity);
	}
}
