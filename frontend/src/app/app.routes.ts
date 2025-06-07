import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { DashboardComponent } from './pages/user/dashboard/dashboard.component';
import { WelcomeComponent } from './pages/user/welcome/welcome.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
   {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'welcome', component: WelcomeComponent },
      { path: '', redirectTo: 'welcome', pathMatch: 'full' },
    ]
  }
];
