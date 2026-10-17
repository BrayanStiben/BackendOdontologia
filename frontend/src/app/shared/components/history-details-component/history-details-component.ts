import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Medicine } from '../../../core/interface/medicine.interface';

@Component({
  imports: [],
  selector: 'app-history-details-component',
  styleUrl: './history-details-component.css',
  templateUrl: './history-details-component.html',
})
export class HistoryDetailsComponent {
  @Input() fecha: string = '';

  @Input() diagnostico: string = '';

  @Input() observaciones: string = '';

  @Input() tratamiento: string = '';

  @Input() medicamentos: Medicine[] = [];

  @Output() cerrar = new EventEmitter<void>();

  cerrarModal(): void {
    this.cerrar.emit();
  }
}
