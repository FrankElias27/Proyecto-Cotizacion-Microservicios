import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { DashboardComponent } from './pages/user/dashboard/dashboard.component';
import { WelcomeComponent } from './pages/user/welcome/welcome.component';
import { ClientComponent } from './pages/user/client/client.component';
import { ProductComponent } from './pages/user/product/product.component';
import { QuotationComponent } from './pages/user/quotation/quotation.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
   {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'welcome', component: WelcomeComponent },
      { path: 'client', component: ClientComponent },
      { path: 'product', component: ProductComponent },
      { path: 'quotation', component: QuotationComponent },
      { path: '', redirectTo: 'welcome', pathMatch: 'full' },
    ]
  }
];
