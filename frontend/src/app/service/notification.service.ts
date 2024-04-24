import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, catchError, Observable, of, Subscription } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { NotificationsResponse } from '../model/notification.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationService implements OnDestroy {
  private notificationsResponseSubject: BehaviorSubject<NotificationsResponse> = new BehaviorSubject<NotificationsResponse>({
    notifications: [],
    totalCount: 0
  });
  notificationsResponseObservable: Observable<NotificationsResponse> = this.notificationsResponseSubject.asObservable();

  private notificationsResponseSubscription: Subscription | null = null;

  constructor(private readonly http: HttpClient) { }

  public fetchNotifications(vehicleId: number, pageIndex: number, pageSize: number): void {
    const options = {
      params: new HttpParams()
        .set('vehicleId', vehicleId)
        .set('pageNumber', pageIndex)
        .set('pageSize', pageSize)
    };

    this.notificationsResponseSubscription =
      this.http.get<NotificationsResponse>(environment.backendUrl + '/notifications', options)
        .pipe(
          catchError((error) => {
            console.error(error);
            return of();
          })
        )
        .subscribe((notificationsResponse: NotificationsResponse) =>
          this.notificationsResponseSubject.next(notificationsResponse));
  }

  ngOnDestroy(): void {
    if (this.notificationsResponseSubscription) {
      this.notificationsResponseSubscription.unsubscribe();
    }
  }
}
