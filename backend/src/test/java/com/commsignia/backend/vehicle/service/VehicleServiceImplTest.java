package com.commsignia.backend.vehicle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import com.commsignia.backend.vehicle.dto.RegisterResponse;
import com.commsignia.backend.vehicle.dto.Vehicle;
import com.commsignia.backend.vehicle.dto.VehiclesResponse;
import com.commsignia.backend.vehicle.entity.VehicleEntity;
import com.commsignia.backend.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

	private static final Long VEHICLE_ID_A = 101L;

	private static final Long VEHICLE_ID_B = 102L;

	@Mock
	private VehicleRepository vehicleRepository;

	private VehicleService vehicleService;

	@BeforeEach
	void setup() {
		vehicleService = new VehicleServiceImpl(vehicleRepository);
	}

	@Test
	void getVehiclesInRadiusShouldCallRepositoryWithCorrectParameters() {
		vehicleService.getVehiclesInRadius(1D, 2D, 3L);
		verify(vehicleRepository, times(1)).getVehiclesInRadius(1D, 2D, 3L);
	}

	@Test
	void getVehiclesInRadiusShouldMapSingleDataCorrectlyToVehicle() {
		VehicleEntity vehicleEntityA = getVehicleEntity(VEHICLE_ID_A, 11D, 12D);
		List<VehicleEntity> vehicleEntities = List.of(vehicleEntityA);
		doReturn(vehicleEntities).when(vehicleRepository).getVehiclesInRadius(1D, 2D, 3L);

		VehiclesResponse vehiclesResponse = vehicleService.getVehiclesInRadius(1D, 2D, 3L);
		assertThat(vehiclesResponse.getVehicles()).hasSize(1);

		Vehicle vehicleA = vehiclesResponse.getVehicles().get(0);
		assertThat(vehicleA.getId()).isEqualTo(VEHICLE_ID_A);
		assertThat(vehicleA.getLatitude()).isEqualTo(11D);
		assertThat(vehicleA.getLongitude()).isEqualTo(12D);
	}

	@Test
	void getVehiclesInRadiusShouldMapMultipleDataCorrectlyToVehicle() {
		VehicleEntity vehicleEntityA = getVehicleEntity(VEHICLE_ID_A, 11D, 12D);
		VehicleEntity vehicleEntityB = getVehicleEntity(VEHICLE_ID_B, 21D, 22D);
		List<VehicleEntity> vehicleEntities = List.of(vehicleEntityA, vehicleEntityB);
		doReturn(vehicleEntities).when(vehicleRepository).getVehiclesInRadius(1D, 2D, 3L);

		VehiclesResponse vehiclesResponse = vehicleService.getVehiclesInRadius(1D, 2D, 3L);
		assertThat(vehiclesResponse.getVehicles()).hasSize(2);

		Vehicle vehicleA = vehiclesResponse.getVehicles().get(0);
		assertThat(vehicleA.getId()).isEqualTo(VEHICLE_ID_A);
		assertThat(vehicleA.getLatitude()).isEqualTo(11D);
		assertThat(vehicleA.getLongitude()).isEqualTo(12D);

		Vehicle vehicleB = vehiclesResponse.getVehicles().get(1);
		assertThat(vehicleB.getId()).isEqualTo(VEHICLE_ID_B);
		assertThat(vehicleB.getLatitude()).isEqualTo(21D);
		assertThat(vehicleB.getLongitude()).isEqualTo(22D);
	}

	@Test
	void registerVehicleShouldCallRepositoryWithCorrectParameters() {
		VehicleEntity emptyVehicleEntity = new VehicleEntity();
		VehicleEntity vehicleEntityA = getVehicleEntity(VEHICLE_ID_A, 11D, 12D);
		doReturn(vehicleEntityA).when(vehicleRepository).save(emptyVehicleEntity);

		vehicleService.registerVehicle();
		verify(vehicleRepository, times(1)).save(emptyVehicleEntity);
	}

	@Test
	void registerVehicleShouldMapDataCorrectlyToRegisterResponse() {
		VehicleEntity emptyVehicleEntity = new VehicleEntity();
		VehicleEntity vehicleEntityA = getVehicleEntity(VEHICLE_ID_A, 11D, 12D);
		doReturn(vehicleEntityA).when(vehicleRepository).save(emptyVehicleEntity);

		RegisterResponse registerResponse = vehicleService.registerVehicle();
		assertThat(registerResponse.getId()).isEqualTo(VEHICLE_ID_A);
	}

	private VehicleEntity getVehicleEntity(Long id, Double latitude, Double longitude) {
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity.setId(id);
		vehicleEntity.setLatitude(latitude);
		vehicleEntity.setLongitude(longitude);
		return vehicleEntity;
	}
}