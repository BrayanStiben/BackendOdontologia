import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SessionService } from '../../../core/service/session.service';
import { BookingApiService } from '../../../core/service/booking-api.service';
import { DoctorSummary, PublicBookingResponse, PublicSlot } from '../../../core/interface/booking.interface';

interface DiaCalendario {
  number: number;
  date: Date;
  perteneceAlMes: boolean;
}

@Component({
  imports: [CommonModule, FormsModule, RouterLink],
  selector: 'app-appointment-component',
  styleUrl: './appointment-component.css',
  templateUrl: './appointment-component.html',
  standalone: true
})
export class AppointmentComponent implements OnInit {
  private sessionService = inject(SessionService);
  private bookingApi = inject(BookingApiService);

  usuario = this.sessionService.usuarioActual;

  specialties: string[] = [];
  doctors: DoctorSummary[] = [];

  especialidadSeleccionada = 'Odontología General';
  doctorSeleccionadoId: number = 1;

  mesActual: Date = new Date(2026, 8, 1); // Septiembre 2026 como en mockup
  diasCalendario: DiaCalendario[] = [];
  fechaSeleccionada: Date = new Date(2026, 8, 10); // 10 de Septiembre 2026
  horaSeleccionada = '10:00 AM';

  horariosDisponibles: string[] = ['08:30 AM', '10:00 AM', '02:30 PM', '04:00 PM'];

  // Datos del Paciente (Sección 3 del Mockup Página 4)
  nombreCompleto = 'María García Pérez';
  numeroDocumento = '1094821402';
  telefono = '+57 312 456 7890';
  email = 'maria.garcia@email.com';
  notificacionesWhatsApp = true;

  // Estado del flujo
  bookingInProgress = false;
  bookingSuccess: PublicBookingResponse | null = null;
  errorMessage = '';

  ngOnInit(): void {
    // Si hay usuario logueado con datos reales, prellenamos
    const u = this.usuario();
    if (u && u.nombre && u.nombre !== 'Juan Sebastian Gonzalez Mancera') {
      this.nombreCompleto = u.nombre;
      this.numeroDocumento = u.id ? u.id.toString() : this.numeroDocumento;
      this.telefono = u.phone || this.telefono;
      this.email = u.correo || this.email;
    }

    this.cargarEspecialidades();
    this.generarCalendario();
  }

  cargarEspecialidades(): void {
    this.bookingApi.getSpecialties().subscribe(specs => {
      this.specialties = specs;
      if (specs.length > 0 && !this.especialidadSeleccionada) {
        this.especialidadSeleccionada = specs[0];
      }
      this.cargarDoctores();
    });
  }

  cargarDoctores(): void {
    this.bookingApi.getDoctors(this.especialidadSeleccionada).subscribe(docs => {
      this.doctors = docs;
      if (docs.length > 0) {
        this.doctorSeleccionadoId = docs[0].id;
        this.cargarHorarios();
      }
    });
  }

  onEspecialidadChange(): void {
    this.cargarDoctores();
  }

  cargarHorarios(): void {
    const y = this.fechaSeleccionada.getFullYear();
    const m = String(this.fechaSeleccionada.getMonth() + 1).padStart(2, '0');
    const d = String(this.fechaSeleccionada.getDate()).padStart(2, '0');
    const isoDate = `${y}-${m}-${d}`;

    this.bookingApi.getAvailableSlots(this.doctorSeleccionadoId, isoDate).subscribe(slots => {
      if (slots && slots.length > 0) {
        this.horariosDisponibles = slots.map(s => s.timeFormatted);
        if (!this.horariosDisponibles.includes(this.horaSeleccionada)) {
          this.horaSeleccionada = this.horariosDisponibles[0];
        }
      } else {
        this.horariosDisponibles = ['08:30 AM', '10:00 AM', '02:30 PM', '04:00 PM'];
      }
    });
  }

  generarCalendario(): void {
    const year = this.mesActual.getFullYear();
    const month = this.mesActual.getMonth();
    const primerDia = new Date(year, month, 1);
    const ultimoDia = new Date(year, month + 1, 0);

    let startDayOfWeek = primerDia.getDay(); // 0 = Domingo
    const dias: DiaCalendario[] = [];

    // Rellenar días del mes anterior
    for (let i = 0; i < startDayOfWeek; i++) {
      dias.push({ number: 0, date: new Date(), perteneceAlMes: false });
    }

    // Rellenar días del mes
    for (let d = 1; d <= ultimoDia.getDate(); d++) {
      dias.push({
        number: d,
        date: new Date(year, month, d),
        perteneceAlMes: true
      });
    }

    this.diasCalendario = dias;
  }

  mesAnterior(): void {
    this.mesActual = new Date(this.mesActual.getFullYear(), this.mesActual.getMonth() - 1, 1);
    this.generarCalendario();
  }

  mesSiguiente(): void {
    this.mesActual = new Date(this.mesActual.getFullYear(), this.mesActual.getMonth() + 1, 1);
    this.generarCalendario();
  }

  seleccionarFecha(fecha: Date): void {
    this.fechaSeleccionada = new Date(fecha);
    this.cargarHorarios();
  }

  seleccionarHora(hora: string): void {
    this.horaSeleccionada = hora;
  }

  esFechaSeleccionada(fecha: Date): boolean {
    if (!this.fechaSeleccionada) return false;
    return (
      fecha.getFullYear() === this.fechaSeleccionada.getFullYear() &&
      fecha.getMonth() === this.fechaSeleccionada.getMonth() &&
      fecha.getDate() === this.fechaSeleccionada.getDate()
    );
  }

  get nombreDoctorSeleccionado(): string {
    const doc = this.doctors.find(d => d.id === Number(this.doctorSeleccionadoId));
    return doc ? `Dr. ${doc.name} ${doc.lastName}` : 'Dr. Alex González';
  }

  get nombreMesActual(): string {
    return this.mesActual.toLocaleDateString('en-US', { month: 'long', year: 'numeric' }).toUpperCase();
  }

  get fechaFormateada(): string {
    if (!this.fechaSeleccionada) return '';
    const formatted = this.fechaSeleccionada.toLocaleDateString('es-CO', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric'
    });
    return formatted.charAt(0).toUpperCase() + formatted.slice(1);
  }

  confirmarCita(): void {
    if (!this.nombreCompleto.trim() || !this.numeroDocumento.trim() || !this.telefono.trim()) {
      this.errorMessage = 'Por favor completa todos tus datos personales.';
      return;
    }

    this.bookingInProgress = true;
    this.errorMessage = '';

    const parts = this.nombreCompleto.trim().split(' ');
    const firstName = parts[0] || 'María';
    const lastName = parts.slice(1).join(' ') || 'García';

    const y = this.fechaSeleccionada.getFullYear();
    const m = String(this.fechaSeleccionada.getMonth() + 1).padStart(2, '0');
    const d = String(this.fechaSeleccionada.getDate()).padStart(2, '0');
    const startIso = `${y}-${m}-${d}T10:00:00Z`;
    const endIso = `${y}-${m}-${d}T10:45:00Z`;

    this.bookingApi.book({
      patientId: Number(this.numeroDocumento.replace(/\D/g, '')) || 1094821402,
      patientName: firstName,
      patientLastName: lastName,
      patientPhone: this.telefono,
      patientEmail: this.email,
      doctorId: Number(this.doctorSeleccionadoId) || 1,
      startsAt: startIso,
      endsAt: endIso,
      receiveWhatsAppNotification: this.notificacionesWhatsApp
    }).subscribe({
      next: (res) => {
        this.bookingInProgress = false;
        this.bookingSuccess = res;
      },
      error: () => {
        this.bookingInProgress = false;
        this.errorMessage = 'Hubo un error al procesar la reserva. Intenta de nuevo.';
      }
    });
  }

  cerrarModalExito(): void {
    this.bookingSuccess = null;
  }
}
