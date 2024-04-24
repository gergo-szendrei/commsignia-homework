import { TestBed } from '@angular/core/testing';

import { VehicleService } from './vehicle.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { Vehicle, VehiclesResponse } from '../model/vehicle.model';

describe('VehicleService', () => {
	let service: VehicleService;
	let httpController: HttpTestingController;

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports: [
				HttpClientTestingModule
			]
		});
		service = TestBed.inject(VehicleService);
		httpController = TestBed.inject(HttpTestingController);
	});

	it('should create service', () => {
		expect(service).toBeTruthy();
	});

	it('should fetch initial value from subject', () => {
		const initialVehicles: Vehicle[] = [];

		service.vehiclesObservable.subscribe((vehicles: Vehicle[]) => {
			expect(vehicles).toEqual(initialVehicles);
		});
	});

	it('should fetch new value from backend', () => {
		const mockVehiclesResponse: VehiclesResponse = {
			vehicles: [
				{
					id: 1,
					latitude: 2,
					longitude: 3
				}
			]
		};

		service.fetchAllVehicles();

		const request = httpController.expectOne({
			method: 'GET',
			url: environment.backendUrl + '/vehicles/all'
		});

		request.flush(mockVehiclesResponse);

		service.vehiclesObservable.subscribe((vehicles: Vehicle[]) => {
			expect(vehicles).toEqual(mockVehiclesResponse.vehicles);
		});
	});
});
