import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar-component/navbar-component';
import { SessionService } from '../../../core/service/session.service';

@Component({
  imports: [CommonModule, FormsModule, NavbarComponent, RouterLink],
  selector: 'app-register-component',
  styleUrl: './register-component.css',
  templateUrl: './register-component.html',
  standalone: true,
})
export class RegisterComponent {
  private router = inject(Router);
  private sessionService = inject(SessionService);

  fullName = 'Alex Gonzalez';
  document = '1.094.821.402';
  email = 'dr.gonzalez@doc-tooth.com';
  phone = '+57 300 123 4567';
  role = 'Odontologo / Especialista';
  professionalCard = 'OD-89421-TP';
  clinicName = 'Clinica Dental OdontoSmile';
  password = '••••••••••••';
  confirmPassword = '••••••••••••';
  acceptTerms = false;
  acceptHabeasData = false;

  isLoading = false;
  errorMessage = '';
  successMessage = '';

  onSubmit(event?: Event): void {
    if (event) {
      event.preventDefault();
    }

    if (!this.fullName.trim() || !this.document.trim() || !this.email.trim()) {
      this.errorMessage = 'Por favor completa todos los campos obligatorios.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }

    if (!this.acceptTerms || !this.acceptHabeasData) {
      this.errorMessage = 'Debes aceptar los términos y el tratamiento de datos personales.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    setTimeout(() => {
      this.isLoading = false;
      this.successMessage = '¡Registro completado exitosamente! Redirigiendo...';
      this.sessionService.actualizarUsuario({
        id: 1,
        nombre: this.fullName,
        correo: this.email,
        phone: this.phone
      });
      setTimeout(() => {
        this.router.navigate(['/dashboard']);
      }, 1000);
    }, 600);
  }
}
