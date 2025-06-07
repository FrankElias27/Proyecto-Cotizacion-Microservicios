import { Component } from '@angular/core';
import { SidebarComponent } from "../sidebar/sidebar.component";
import { AppComponent } from "../../../app.component";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SidebarComponent, AppComponent,RouterOutlet],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

}
