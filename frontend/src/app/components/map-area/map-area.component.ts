import { AfterViewInit, Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { Vehicle } from '../../model/vehicle.model';
import { VehicleService } from '../../service/vehicle.service';
import { Subscription } from 'rxjs';
import { GoogleMap } from '@angular/google-maps';

@Component({
  selector: 'app-map-area',
  templateUrl: './map-area.component.html',
  styleUrl: './map-area.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class MapAreaComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild(GoogleMap) googleMap: GoogleMap | undefined;
  zoom: number = 12;
  center: google.maps.LatLngLiteral = { lat: 47.47581, lng: 19.05749 };
  options: google.maps.MapOptions = {
    fullscreenControl: false,
    mapTypeControl: false,
    streetViewControl: false,
    gestureHandling: 'none',
    zoomControl: false,
    scrollwheel: false,
    disableDoubleClickZoom: true,
    maxZoom: 15,
    minZoom: 9
  };

  @Output() readonly markerClickEmit = new EventEmitter<number>();

  vehicles: Vehicle[] = [];
  private readonly refreshRateMs: number = 5000;
  private refreshInterval = 0;
  private vehiclesSubscription: Subscription | null = null;

  constructor(private vehicleService: VehicleService) {
  }

  ngOnInit(): void {
    this.vehicleService.fetchAllVehicles();
  }

  ngAfterViewInit(): void {
    this.refreshInterval = setInterval(() => {
      this.vehicleService.fetchAllVehicles();
    }, this.refreshRateMs);
    this.vehiclesSubscription = this.vehicleService.vehiclesObservable.subscribe(
      async (vehicles: Vehicle[]) => {
        this.vehicles = vehicles;
        await this.fitMapBounds();
      });
  }

  ngOnDestroy(): void {
    clearInterval(this.refreshInterval);
    if (this.vehiclesSubscription) {
      this.vehiclesSubscription.unsubscribe();
    }
  }

  handleMarkerClick(vehicleId: number): void {
    this.markerClickEmit.emit(vehicleId);
  }

  private async fitMapBounds(): Promise<void> {
    const { LatLngBounds } = await google.maps.importLibrary('core') as google.maps.CoreLibrary;
    const bounds: google.maps.LatLngBounds = new LatLngBounds();

    this.vehicles.forEach((vehicle: Vehicle) => {
      bounds.extend({ lat: vehicle.latitude, lng: vehicle.longitude });
    });

    this.center = { lat: bounds.getCenter().lat(), lng: bounds.getCenter().lng() };
    this.googleMap?.fitBounds(bounds);
    if (this.googleMap?.getZoom() !== undefined) {
      this.zoom = this.googleMap?.getZoom() as number;
    }
  }
}
