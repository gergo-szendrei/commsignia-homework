import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Vehicle, VehiclesResponse } from '../model/vehicle.model';
import { BehaviorSubject, catchError, Observable, of, Subscription } from 'rxjs';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class VehicleService implements OnDestroy {
  private vehiclesSubject: BehaviorSubject<Vehicle[]> = new BehaviorSubject<Vehicle[]>([]);
  vehiclesObservable: Observable<Vehicle[]> = this.vehiclesSubject.asObservable();

  private vehiclesSubscription: Subscription | null = null;

  constructor(private readonly http: HttpClient) { }

  public fetchAllVehicles(): void {
    this.vehiclesSubscription = this.http.get<VehiclesResponse>(environment.backendUrl + '/vehicles/all')
      .pipe(
        catchError((error) => {
          console.error(error);
          return of();
        })
      )
      .subscribe((vehiclesResponse: VehiclesResponse) => this.vehiclesSubject.next(vehiclesResponse.vehicles));
  }

  ngOnDestroy(): void {
    if (this.vehiclesSubscription) {
      this.vehiclesSubscription.unsubscribe();
    }
  }
}
