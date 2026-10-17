import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar-component/navbar-component';
import { SessionService } from '../../../core/service/session.service';

@Component({
  imports: [CommonModule, FormsModule, NavbarComponent, RouterLink],
  selector: 'app-login-component',
  styleUrl: './login-component.css',
  templateUrl: './login-component.html',
  standalone: true,
})
export class LoginComponent {
  private router = inject(Router);
  private sessionService = inject(SessionService);

  emailOrUser = 'dr.gonzalez@doc-tooth.com';
  password = '••••••••••••';
  rememberMe = true;
  isLoading = false;
  errorMessage = '';

  // Modal Olvidó su contraseña
  showForgotPasswordModal = false;
  forgotEmail = '';
  forgotSuccessMessage = '';

  onLogin(event?: Event): void {
    if (event) {
      event.preventDefault();
    }

    if (!this.emailOrUser.trim() || !this.password) {
      this.errorMessage = 'Por favor ingresa tu usuario/correo y contraseña.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    setTimeout(() => {
      this.isLoading = false;
      const lower = this.emailOrUser.toLowerCase();
      if (lower.includes('dr') || lower.includes('admin') || lower.includes('odontologo')) {
        this.sessionService.actualizarUsuario({
          id: 1,
          nombre: 'Dr. Alex González',
          correo: this.emailOrUser,
          phone: '+57 300 123 4567'
        });
        this.router.navigate(['/dashboard']);
      } else {
        this.sessionService.actualizarUsuario({
          id: 1094821402,
          nombre: 'María García Pérez',
          correo: this.emailOrUser,
          phone: '+57 312 456 7890'
        });
        this.router.navigate(['/patient-dashboard/home']);
      }
    }, 400);
  }

  abrirOlvidoContrasena(): void {
    this.forgotEmail = this.emailOrUser.includes('@') ? this.emailOrUser : '';
    this.forgotSuccessMessage = '';
    this.showForgotPasswordModal = true;
  }

  cerrarOlvidoContrasena(): void {
    this.showForgotPasswordModal = false;
  }

  enviarRecuperacion(): void {
    if (!this.forgotEmail || !this.forgotEmail.includes('@')) {
      alert('Ingresa un correo electrónico válido');
      return;
    }
    this.forgotSuccessMessage = `Se ha enviado un enlace de restablecimiento a ${this.forgotEmail}`;
    setTimeout(() => {
      this.showForgotPasswordModal = false;
    }, 2000);
  }
}
