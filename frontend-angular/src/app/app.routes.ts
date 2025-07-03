import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { ProductosComponent } from './productos/productos.component';
import { AuthGuard } from './auth/auth.guard';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'productos', component: ProductosComponent, canActivate: [AuthGuard] }
];
