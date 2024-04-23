export interface NotificationsResponse {
  notifications: Notification[];
  totalCount: number;
}

export interface Notification {
  id: number;
  vehicleId: number;
  message: string;
}
