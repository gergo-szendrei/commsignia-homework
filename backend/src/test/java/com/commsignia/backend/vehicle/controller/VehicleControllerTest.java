package com.commsignia.backend.vehicle.controller;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.commsignia.backend.vehicle.dto.RegisterResponse;
import com.commsignia.backend.vehicle.dto.Vehicle;
import com.commsignia.backend.vehicle.dto.VehiclesResponse;
import com.commsignia.backend.vehicle.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

	private static final Long VEHICLE_ID_A = 101L;

	private static final Long VEHICLE_ID_B = 102L;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private VehicleService vehicleService;

	@Test
	void getVehiclesInRadiusShouldPassCorrectParametersAndReturnCorrectData() throws Exception {
		Vehicle vehicleA = Vehicle.builder().id(VEHICLE_ID_A).latitude(11D).longitude(12D).build();
		Vehicle vehicleB = Vehicle.builder().id(VEHICLE_ID_B).latitude(21D).longitude(22D).build();
		List<Vehicle> vehicles = List.of(vehicleA, vehicleB);
		VehiclesResponse vehiclesResponse = VehiclesResponse.builder().vehicles(vehicles).build();
		doReturn(vehiclesResponse).when(vehicleService).getVehiclesInRadius(1D, 2D, 3L);

		mvc.perform(MockMvcRequestBuilders
						.get("/vehicles")
						.param("latitude", "1")
						.param("longitude", "2")
						.param("radius", "3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicles.[0].id").value(VEHICLE_ID_A))
				.andExpect(jsonPath("$.vehicles.[0].latitude").value(11D))
				.andExpect(jsonPath("$.vehicles.[0].longitude").value(12D))
				.andExpect(jsonPath("$.vehicles.[1].id").value(VEHICLE_ID_B))
				.andExpect(jsonPath("$.vehicles.[1].latitude").value(21D))
				.andExpect(jsonPath("$.vehicles.[1].longitude").value(22D));
		verify(vehicleService, times(1)).getVehiclesInRadius(1D, 2D, 3L);
	}

	@Test
	void registerVehicleShouldPassCorrectParametersAndReturnCorrectData() throws Exception {
		RegisterResponse registerResponse = RegisterResponse.builder().id(VEHICLE_ID_A).build();
		doReturn(registerResponse).when(vehicleService).registerVehicle();

		mvc.perform(MockMvcRequestBuilders
						.post("/vehicles"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(VEHICLE_ID_A));
		verify(vehicleService, times(1)).registerVehicle();
	}
}
