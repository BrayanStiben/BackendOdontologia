import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Medicine } from '../../../core/interface/medicine.interface';

interface DetalleMedicamento {
  medicamentoId: number;
  nombre: string;
  unidades: number | null;
}

@Component({
  imports: [CommonModule, FormsModule],
  selector: 'app-diagnose-component',
  styleUrl: './diagnose-component.css',
  templateUrl: './diagnose-component.html',
  standalone: true,
})
export class DiagnoseComponent {
  // Se asignará cuando se determine el paciente de la consulta.
  paciente: any | null = null;

  buscandoPaciente = false;
  mensajePaciente = '';

  diagnostico = '';
  observaciones = '';
  tratamiento = '';

  // La lista se llenará con los resultados del servicio HTTP.
  resultadosMedicamentos: Medicine[] = [];
  busquedaMedicamento = '';

  // Medicamentos seleccionados para el tratamiento actual.
  detallesTratamiento: DetalleMedicamento[] = [];

  unidadesMedicamento: number | null = 1;
  medicamentoSeleccionado: Medicine | null = null;

  cargandoMedicamentos = false;
  registrando = false;
  mensajeError = '';
  mensajeMedicamentos = '';
  mensajeExito = '';

  onDocumentoChange(documento: string): void {

  }

  buscarMedicamentos(): void {
    const termino = this.busquedaMedicamento.trim();

    this.mensajeMedicamentos = '';
    this.mensajeError = '';
    this.medicamentoSeleccionado = null;
    this.resultadosMedicamentos = [];

    if (!termino) {
      this.mensajeMedicamentos = 'Escribe el nombre del medicamento que deseas buscar.';
      return;
    }

    /*
     * PENDIENTE: integrar el servicio HTTP de medicamentos.
     *
     * La API debe buscar por nombre y devolver los medicamentos
     * disponibles en el inventario o catálogo autorizado.
     *
     * No se agregan resultados ficticios.
     */
    this.mensajeMedicamentos =
      'La búsqueda de medicamentos estará disponible al conectar el backend.';
  }

  seleccionarMedicamento(medicamento: Medicine): void {
    this.medicamentoSeleccionado = medicamento;
    this.unidadesMedicamento = 1;
    this.mensajeError = '';
    this.mensajeMedicamentos = '';
  }

  agregarMedicamento(): void {
    if (!this.medicamentoSeleccionado) {
      this.mensajeError = 'Selecciona un medicamento de los resultados.';
      return;
    }

    if (
      this.unidadesMedicamento === null ||
      !Number.isInteger(this.unidadesMedicamento) ||
      this.unidadesMedicamento < 1
    ) {
      this.mensajeError = 'Ingresa una cantidad de unidades válida (número entero mayor que cero).';
      return;
    }

    const existente = this.detallesTratamiento.find(
      (detalle) => detalle.medicamentoId === this.medicamentoSeleccionado!.ID,
    );

    if (existente) {
      // @ts-ignore
      existente.unidades += this.unidadesMedicamento;
    } else {
      this.detallesTratamiento = [
        ...this.detallesTratamiento,
        {
          medicamentoId: this.medicamentoSeleccionado.ID,
          nombre: this.medicamentoSeleccionado.MedicineName,
          unidades: this.unidadesMedicamento,
        },
      ];
    }

    this.medicamentoSeleccionado = null;
    this.busquedaMedicamento = '';
    this.unidadesMedicamento = 1;
    this.resultadosMedicamentos = [];
    this.mensajeMedicamentos = '';
    this.mensajeError = '';
  }

  eliminarMedicamento(medicamentoId: number): void {
    this.detallesTratamiento = this.detallesTratamiento.filter(
      (detalle) => detalle.medicamentoId !== medicamentoId,
    );
  }

  registrarDiagnostico(): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.paciente) {
      this.mensajeError = 'Debes seleccionar un paciente antes de registrar el diagnóstico.';
      return;
    }

    if (!this.diagnostico.trim()) {
      this.mensajeError = 'El campo Diagnóstico es obligatorio.';
      return;
    }

    if (!this.tratamiento.trim()) {
      this.mensajeError = 'El campo Tratamiento es obligatorio.';
      return;
    }

    if (this.registrando) {
      return;
    }

    const datosDiagnostico = {
      pacienteId: this.paciente.id,
      diagnostico: this.diagnostico.trim(),
      observaciones: this.observaciones.trim(),
      tratamiento: this.tratamiento.trim(),
      medicamentos: this.detallesTratamiento.map((detalle) => ({
        medicamentoId: detalle.medicamentoId,
        unidades: detalle.unidades,
      })),
    };

    /*
     * PENDIENTE: enviar datosDiagnostico al backend mediante HTTP.
     *
     * Al completar la integración:
     * - activar registrando durante la solicitud;
     * - enviar el objeto al endpoint correspondiente;
     * - mostrar confirmación solo si el servidor responde correctamente;
     * - gestionar errores y restablecer registrando.
     *
     * No se simula el registro ni se muestra un éxito ficticio.
     */
    console.log('Estructura preparada para la API:', datosDiagnostico);

    this.mensajeError =
      'El registro del diagnóstico estará disponible cuando se conecte el backend.';
  }
}
