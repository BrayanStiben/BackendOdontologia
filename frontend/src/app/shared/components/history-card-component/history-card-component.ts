import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-history-card-component',
  styleUrl: './history-card-component.css',
  templateUrl: './history-card-component.html',
})
export class HistoryCardComponent {
  @Input() fecha: string = '';

  @Input() diagnostico: string = '';

  @Input() observaciones: string = '';

  @Input() tratamiento: string = '';

  @Output() verDetalle = new EventEmitter<void>();
}
