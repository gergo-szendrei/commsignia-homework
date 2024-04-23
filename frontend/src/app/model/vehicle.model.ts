export interface VehiclesResponse {
  vehicles: Vehicle[];
}

export interface Vehicle {
  id: number;
  latitude: number;
  longitude: number;
}
