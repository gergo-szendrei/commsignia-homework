import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationAreaComponent } from './notification-area.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NotificationService } from '../../service/notification.service';
import { of } from 'rxjs';
import { NotificationsResponse } from '../../model/notification.model';

describe('NotificationAreaComponent', () => {
  let component: NotificationAreaComponent;
  let fixture: ComponentFixture<NotificationAreaComponent>;

  const vehicleId: number = 2;
  const pageIndex: number = 0;
  const pageSize: number = 25;

  const mockNotificationsResponse: NotificationsResponse = {
    notifications: [
      {
        id: 1,
        vehicleId,
        message: 'A'
      }
    ],
    totalCount: 1
  };

  const mockNotificationService = {
    notificationsResponseObservable: of(mockNotificationsResponse),
    // It is used, do not remove
    fetchNotifications: (vehicleIdParam: number, pageIndexParam: number, pageSizeParam: number) => {
      expect(vehicleIdParam).toEqual(vehicleId);
      expect(pageIndexParam).toEqual(pageIndex);
      expect(pageSizeParam).toEqual(pageSize);
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        MatPaginatorModule
      ],
      declarations: [
        NotificationAreaComponent
      ],
      providers: [{ provide: NotificationService, useValue: mockNotificationService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(NotificationAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should populate fields correctly after fetching', () => {
    expect(component.notifications).toEqual(mockNotificationsResponse.notifications);
    expect(component.totalCount).toEqual(mockNotificationsResponse.totalCount);
    expect(component.vehicleId).toEqual(mockNotificationsResponse.notifications[0].vehicleId);
  });

  it('should populate fields correctly and start fetching after handleMarkerClick', () => {
    component.pageSize = pageSize;
    component.handleMarkerClick(vehicleId);
    expect(component.vehicleId).toEqual(vehicleId);
    expect(component.pageIndex).toEqual(pageIndex);
  });

  it('should populate fields correctly and start fetching after handlePageEvent', () => {
    component.handlePageEvent({
      pageIndex: pageIndex,
      pageSize: pageSize,
      length: 1
    });
    expect(component.pageIndex).toEqual(pageIndex);
    expect(component.pageSize).toEqual(pageSize);
  });
});
