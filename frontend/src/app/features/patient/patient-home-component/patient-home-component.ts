import { Component, inject } from '@angular/core';
import { SessionService } from '../../../core/service/session.service';
import { FormsModule } from '@angular/forms';

@Component({
  imports: [FormsModule],
  selector: 'app-patient-home-component',
  styleUrl: './patient-home-component.css',
  templateUrl: './patient-home-component.html',
  standalone: true,
})
export class PatientHomeComponent {
  //Usuario logueado

  private sessionService = inject(SessionService);
  usuario = this.sessionService.usuarioActual;

  // Valores originales para detectar modificaciones

  private correoOriginal: string = this.usuario().correo;

  private telefonoOriginal: string = this.usuario().phone;

  // Campos editables

  correo: string = this.usuario().correo;

  telefono: string = this.usuario().phone;

  // ============================
  // CITA ACTUAL
  // ============================

  citaActual: any = {
    especialidad: 'Odontología general',
    profesional: 'Dr. Carlos Rodríguez',
    fecha: '15 de octubre de 2026',
    hora: '10:00 AM',
    estado: 'Agendada',
  };

  // ============================
  // DETECTAR CAMBIOS
  // ============================

  get datosModificados(): boolean {
    return this.correo !== this.correoOriginal || this.telefono !== this.telefonoOriginal;
  }

  // ============================
  // ACTUALIZAR DATOS
  // ============================

  actualizarDatos(): void {
    if (!this.datosModificados) {
      return;
    }

    const usuarioActual = this.sessionService.usuarioActual();

    const usuarioActualizado = {
      ...usuarioActual,

      correo: this.correo,

      phone: this.telefono,
    };

    this.sessionService.actualizarUsuario(usuarioActualizado);
  }

  // ============================
  // REFRESCAR CITA
  // ============================

  refrescarCita(): void {}

  // ============================
  // CANCELAR CITA
  // ============================

  cancelarCita(): void {}
}
