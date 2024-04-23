import { Component, ViewChild, ViewEncapsulation } from '@angular/core';
import { NotificationAreaComponent } from './components/notification-area/notification-area.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  @ViewChild(NotificationAreaComponent) notificationAreaComponent: NotificationAreaComponent | undefined;
  title = 'frontend';

  onMarkerClickEmit(vehicleId: number): void {
    this.notificationAreaComponent?.handleMarkerClick(vehicleId);
  }
}
