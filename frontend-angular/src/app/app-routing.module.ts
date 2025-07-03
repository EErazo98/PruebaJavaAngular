import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from './guards/auth.guard'; // Protege rutas privadas
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ProductPageComponent } from './pages/product-page/product-page.component';

const routes: Routes = [
  // Página de login (pública)
  { path: 'login', component: LoginPageComponent },

  // Página de inicio (privada)
  { path: '', component: HomePageComponent, canActivate: [AuthGuard] },

  // Página de productos (privada)
  { path: 'productos', component: ProductPageComponent, canActivate: [AuthGuard] },

  // Cualquier ruta no definida redirige a login
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
