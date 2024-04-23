import { TestBed } from '@angular/core/testing';

import { NotificationService } from './notification.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../environment/environment';
import { NotificationsResponse } from '../model/notification.model';

describe('NotificationService', () => {
	let service: NotificationService;
	let httpController: HttpTestingController;

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports: [
				HttpClientTestingModule
			]
		});
		service = TestBed.inject(NotificationService);
		httpController = TestBed.inject(HttpTestingController);
	});

	it('should create service', () => {
		expect(service).toBeTruthy();
	});

	it('should fetch initial value from subject', () => {
		const initialNotificationsResponse: NotificationsResponse = {
			notifications: [],
			totalCount: 0
		};

		service.notificationsResponseObservable.subscribe((notificationsResponse: NotificationsResponse) => {
			expect(notificationsResponse).toEqual(initialNotificationsResponse);
		});
	});

	it('should fetch new value from backend', () => {
		const mockNotificationsResponse: NotificationsResponse = {
			notifications: [
				{
					id: 1,
					vehicleId: 2,
					message: 'A'
				}
			],
			totalCount: 1
		};

		service.fetchNotifications(1, 0, 50);

		const request = httpController.expectOne({
			method: 'GET',
			url: environment.backendUrl + '/notifications?vehicleId=1&pageNumber=0&pageSize=50'
		});

		request.flush(mockNotificationsResponse);

		service.notificationsResponseObservable.subscribe((notificationsResponse: NotificationsResponse) => {
			expect(notificationsResponse).toEqual(mockNotificationsResponse);
		});
	});
});
