import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { MapAreaComponent } from './components/map/map-area.component';
import { HttpClientModule } from '@angular/common/http';
import { NotificationAreaComponent } from './components/notification-area/notification-area.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    MapAreaComponent,
    NotificationAreaComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    GoogleMapsModule,
    MatPaginatorModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
