import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HistoryCardComponent } from '../../../shared/components/history-card-component/history-card-component';
import { clinicalHistory } from '../../../core/interface/clinicalHistory.interface';

@Component({
  imports: [CommonModule, FormsModule, HistoryCardComponent],
  selector: 'app-consultation-component',
  styleUrl: './consultation-component.css',
  templateUrl: './consultation-component.html',
  standalone: true
})
export class ConsultationComponent {
  // Documento introducido en el buscador.
  documentoBusqueda = '';

  // Información que se recibirá desde el backend.
  paciente: any | null = null;

  // Se mantiene vacío hasta recibir el historial desde la API.
  historialClinico: clinicalHistory[] = [];

  readonly registrosPorPagina = 5;
  paginaActual = 1;

  busquedaRealizada = false;
  cargando = false;
  mensajeError = '';

  get totalPaginas(): number {
    return Math.ceil(this.historialClinico.length / this.registrosPorPagina);
  }

  get registrosPaginados(): clinicalHistory[] {
    const inicio = (this.paginaActual - 1) * this.registrosPorPagina;

    return this.historialClinico.slice(inicio, inicio + this.registrosPorPagina);
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, indice) => indice + 1);
  }

  buscarPaciente(): void {
    const documento = this.documentoBusqueda.trim();

    if (!documento) {
      this.mensajeError = 'Ingresa el número de documento del paciente.';
      return;
    }

    // Reiniciar los datos de la consulta anterior.
    this.paciente = null;
    this.historialClinico = [];
    this.paginaActual = 1;
    this.busquedaRealizada = false;
    this.mensajeError = '';

    /*
     * PENDIENTE: integración con el servicio HTTP.
     *
     * 1. Consultar el paciente por su documento.
     * 2. Obtener su historial clínico.
     * 3. Asignar los resultados a paciente e historialClinico.
     * 4. Gestionar los errores y finalizar el estado de carga.
     *
     * No se insertan registros ficticios ni se simula una respuesta.
     */

    this.mensajeError = 'La búsqueda estará disponible cuando se conecte el servicio del backend.';
  }

  cambiarPagina(pagina: number): void {
    if (pagina < 1 || pagina > this.totalPaginas) {
      return;
    }

    this.paginaActual = pagina;
  }

  paginaAnterior(): void {
    this.cambiarPagina(this.paginaActual - 1);
  }

  paginaSiguiente(): void {
    this.cambiarPagina(this.paginaActual + 1);
  }

  limpiarBusqueda(): void {
    this.documentoBusqueda = '';
    this.paciente = null;
    this.historialClinico = [];
    this.paginaActual = 1;
    this.busquedaRealizada = false;
    this.cargando = false;
    this.mensajeError = '';
  }
}
