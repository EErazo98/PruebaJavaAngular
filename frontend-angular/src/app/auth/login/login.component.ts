import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(): void {
    console.log('Enviando:', { email: this.email, password: this.password });
    this.authService.login({ email: this.email, password: this.password })
      .subscribe({
        next: (res) => {
          console.log('Respuesta del backend:', res);
          this.authService.saveToken(res.token);
          this.router.navigate(['/productos']);
        },
        error: (err) => {
          console.error('Error del backend:', err);
          this.errorMessage = 'Credenciales inválidas';
        }
      });
  }

}
