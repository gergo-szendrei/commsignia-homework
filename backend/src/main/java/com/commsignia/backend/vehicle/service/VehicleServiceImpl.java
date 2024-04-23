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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

	private final VehicleRepository vehicleRepository;

	public VehicleServiceImpl(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public VehiclesResponse getVehiclesInRadius(Double latitude, Double longitude, Long radius) {
		List<Vehicle> vehicles = vehicleRepository.getVehiclesInRadius(latitude, longitude, radius).stream()
				.map(Vehicle::toVehicle)
				.toList();
		return VehiclesResponse.builder().vehicles(vehicles).build();
	}

	@Override
	public RegisterResponse registerVehicle() {
		VehicleEntity vehicleEntity = new VehicleEntity();
		// Assigning it back does not needed for proper operation, but helps testability
		vehicleEntity = vehicleRepository.save(vehicleEntity);
		return RegisterResponse.builder()
				.id(vehicleEntity.getId())
				.build();
	}

	@Override
	public void updatePosition(Long id, Position position) {
		Optional<VehicleEntity> optional = vehicleRepository.findById(id);
		if (optional.isEmpty()) {
			throw new CustomRuntimeException(String.format("Cannot update position. Vehicle by id %1$s cannot be found.", id));
		}
		VehicleEntity vehicleEntity = optional.get();
		vehicleEntity.setLatitude(position.getLatitude());
		vehicleEntity.setLongitude(position.getLongitude());
		// It would save even without calling save explicitly, but this approach helps testability
		vehicleRepository.save(vehicleEntity);
	}
}
