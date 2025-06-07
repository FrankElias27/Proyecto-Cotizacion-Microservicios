import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';
import { HeaderComponent } from "./shared/header/header.component";
import { FooterComponent } from "./shared/footer/footer.component";
import {OidcSecurityService} from "angular-auth-oidc-client";
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
   private readonly oidcSecurityService = inject(OidcSecurityService);
   private readonly router = inject(Router);

  ngOnInit(): void {
    initFlowbite();

    this.oidcSecurityService
      .checkAuth()
      .subscribe(({isAuthenticated}) => {
        console.log('app authenticated', isAuthenticated);
          if (isAuthenticated) {
          this.router.navigate(['/dashboard']);
        }else {
        this.router.navigate(['']);
      }

      })

  }
}
