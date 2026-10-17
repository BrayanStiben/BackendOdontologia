import { Component } from '@angular/core';
import { CommonModule} from '@angular/common';
import { Meeting } from '../../../core/interface/Meeting';

interface DiaCalendario {
  fecha: Date;
  clave: string;
  numero: number;
  perteneceAlMes: boolean;
  esHoy: boolean;
  cantidadCitas: number;
}

@Component({
  imports: [CommonModule],
  standalone: true,
  selector: 'app-calendar-component',
  styleUrl: './calendar-component.css',
  templateUrl: './calendar-component.html',
})
export class CalendarComponent {
  // Se llenará posteriormente con los datos del servicio HTTP.
  citas: Meeting[] = [];

  // Fecha correspondiente al mes que se está visualizando.
  fechaVisible: Date = this.inicioDelDia(new Date());

  // Fecha seleccionada para consultar su agenda.
  fechaSeleccionada: Date | null = null;

  // Cita seleccionada para consultar sus detalles.
  citaDetalle: Meeting | null = null;

  // Controla la vista mensual o la agenda diaria.
  vistaActual: 'mes' | 'dia' = 'mes';

  readonly diasSemana = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];

  readonly meses = [
    'Enero',
    'Febrero',
    'Marzo',
    'Abril',
    'Mayo',
    'Junio',
    'Julio',
    'Agosto',
    'Septiembre',
    'Octubre',
    'Noviembre',
    'Diciembre',
  ];

  private inicioDelDia(fecha: Date): Date {
    return new Date(fecha.getFullYear(), fecha.getMonth(), fecha.getDate());
  }

  private claveFecha(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');

    return `${anio}-${mes}-${dia}`;
  }

  get tituloMes(): string {
    return `${this.meses[this.fechaVisible.getMonth()]} ${this.fechaVisible.getFullYear()}`;
  }

  get tituloFechaSeleccionada(): string {
    if (!this.fechaSeleccionada) {
      return '';
    }

    return new Intl.DateTimeFormat('es-CO', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    }).format(this.fechaSeleccionada);
  }

  get hoyClave(): string {
    return this.claveFecha(new Date());
  }

  get diasCalendario(): DiaCalendario[] {
    const anio = this.fechaVisible.getFullYear();
    const mes = this.fechaVisible.getMonth();

    const primerDia = new Date(anio, mes, 1);

    // JavaScript usa domingo = 0. Convertimos a lunes = 0.
    const desplazamiento = (primerDia.getDay() + 6) % 7;

    const inicio = new Date(anio, mes, 1 - desplazamiento);
    const hoy = this.hoyClave;

    // Se generan 42 celdas para mantener una cuadrícula uniforme de 6 semanas.
    return Array.from({ length: 42 }, (_, indice) => {
      const fecha = new Date(inicio.getFullYear(), inicio.getMonth(), inicio.getDate() + indice);

      const clave = this.claveFecha(fecha);

      return {
        fecha,
        clave,
        numero: fecha.getDate(),
        perteneceAlMes: fecha.getMonth() === mes,
        esHoy: clave === hoy,
        cantidadCitas: this.obtenerCitasDeFecha(clave).length,
      };
    });
  }

  get citasDelDia(): Meeting[] {
    if (!this.fechaSeleccionada) {
      return [];
    }

    const clave = this.claveFecha(this.fechaSeleccionada);

    return this.citas
      .filter((cita) => cita.fecha === clave && cita.estado !== 'Cancelado')
      .slice()
      .sort((a, b) => a.hora.localeCompare(b.hora));
  }

  get pacientesAtendidosDelDia(): number {
    return this.citasDelDia.filter((cita) => cita.estado === 'Atendido').length;
  }

  get citasPendientesDelDia(): number {
    return this.citasDelDia.filter((cita) => cita.estado !== 'Atendido').length;
  }

  obtenerCitasDeFecha(clave: string): Meeting[] {
    return this.citas.filter((cita) => cita.fecha === clave && cita.estado !== 'Cancelado');
  }

  anteriorMes(): void {
    this.fechaVisible = new Date(
      this.fechaVisible.getFullYear(),
      this.fechaVisible.getMonth() - 1,
      1,
    );
  }

  siguienteMes(): void {
    this.fechaVisible = new Date(
      this.fechaVisible.getFullYear(),
      this.fechaVisible.getMonth() + 1,
      1,
    );
  }

  irAHoy(): void {
    const hoy = this.inicioDelDia(new Date());

    this.fechaVisible = hoy;
    this.fechaSeleccionada = hoy;
    this.vistaActual = 'mes';
    this.citaDetalle = null;
  }

  seleccionarDia(dia: DiaCalendario): void {
    this.fechaSeleccionada = dia.fecha;
    this.fechaVisible = new Date(dia.fecha.getFullYear(), dia.fecha.getMonth(), 1);

    this.vistaActual = 'dia';
    this.citaDetalle = null;
  }

  volverAlMes(): void {
    this.vistaActual = 'mes';
    this.citaDetalle = null;
  }

  abrirDetalle(cita: Meeting): void {
    this.citaDetalle = cita;
  }

  cerrarDetalle(): void {
    this.citaDetalle = null;
  }

  detenerPropagacion(evento: Event): void {
    evento.stopPropagation();
  }

  obtenerClaseEstado(estado: Meeting): string {
    switch (estado.estado) {
      case 'Atendido':
        return 'estado-atendido';

      case 'Confirmado':
      case 'Agendado':
        return 'estado-confirmado';

      case 'En espera':
      case 'Pendiente':
        return 'estado-pendiente';

      case 'Cancelado':
        return 'estado-cancelado';

      default:
        return 'estado-pendiente';
    }
  }

  get hayCitasEnMes(): boolean {
    return this.diasCalendario.some((dia) => dia.perteneceAlMes && dia.cantidadCitas > 0);
  }
}
