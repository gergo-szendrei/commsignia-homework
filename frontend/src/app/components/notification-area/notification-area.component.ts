import { Component, OnDestroy, ViewEncapsulation } from '@angular/core';
import { Notification, NotificationsResponse } from '../../model/notification.model';
import { Subscription } from 'rxjs';
import { NotificationService } from '../../service/notification.service';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-notification-area',
  templateUrl: './notification-area.component.html',
  styleUrl: './notification-area.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class NotificationAreaComponent implements OnDestroy {
  notifications: Notification[] = [];
  totalCount: number = 0;
  vehicleId: number = 0;

  pageIndex: number = 0;
  pageSize: number = 50;

  private readonly notificationsSubscription: Subscription;

  constructor(private notificationService: NotificationService) {
    this.notificationsSubscription = this.notificationService.notificationsResponseObservable.subscribe(
      (notificationsResponse: NotificationsResponse) => {
        this.notifications = notificationsResponse.notifications;
        this.totalCount = notificationsResponse.totalCount;
        if (this.notifications.length !== 0) {
          this.vehicleId = this.notifications[0].vehicleId;
        }
      });
  }

  ngOnDestroy(): void {
    if (this.notificationsSubscription) {
      this.notificationsSubscription.unsubscribe();
    }
  }

  handleMarkerClick(vehicleId: number): void {
    this.vehicleId = vehicleId;
    this.pageIndex = 0;
    this.notificationService.fetchNotifications(this.vehicleId, this.pageIndex, this.pageSize);
  }

  handlePageEvent(event: PageEvent): void {
    if (this.vehicleId) {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
      this.notificationService.fetchNotifications(this.vehicleId, this.pageIndex, this.pageSize);
    }
  }
}
