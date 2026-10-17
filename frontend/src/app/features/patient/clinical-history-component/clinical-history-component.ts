import { Component } from '@angular/core';
import { HistoryCardComponent } from '../../../shared/components/history-card-component/history-card-component';
import { HistoryDetailsComponent } from '../../../shared/components/history-details-component/history-details-component';
import { clinicalHistory } from '../../../core/interface/clinicalHistory.interface';

@Component({
  imports: [HistoryCardComponent, HistoryDetailsComponent],
  selector: 'app-clinical-history-component',
  styleUrl: './clinical-history-component.css',
  templateUrl: './clinical-history-component.html',
})
export class ClinicalHistoryComponent {
  clinicalHistory: clinicalHistory[] = [
    {
      historyId: 1,
      patientId: 1,
      fecha: '21/10/2021',
      diagnostico: 'asm',
      observaciones: 'ksk',
      tratamiento: '',
    },

  ];

  mostrarDetalle = false;

  historialSeleccionado: any = null;

  abrirDetalle(historial: any): void {
    this.historialSeleccionado = historial;

    this.mostrarDetalle = true;
  }

  cerrarDetalle(): void {
    this.mostrarDetalle = false;

    this.historialSeleccionado = null;
  }
}
