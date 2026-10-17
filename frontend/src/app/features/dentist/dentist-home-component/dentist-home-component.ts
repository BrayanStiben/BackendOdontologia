import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { DashboardApiService } from '../../../core/service/dashboard-api.service';
import { DashboardSummary, WaitingPatient } from '../../../core/interface/dashboard.interface';

@Component({
  imports: [CommonModule, FormsModule, RouterLink],
  selector: 'app-dentist-home-component',
  styleUrl: './dentist-home-component.css',
  templateUrl: './dentist-home-component.html',
  standalone: true
})
export class DentistHomeComponent implements OnInit {
  private dashboardService = inject(DashboardApiService);
  private router = inject(Router);

  searchQuery = '';
  summary: DashboardSummary | null = null;
  loading = true;
  restocking = false;
  restockSuccess = '';

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard(): void {
    this.loading = true;
    this.dashboardService.getSummary().subscribe({
      next: (data) => {
        this.summary = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  get pacientesFiltrados(): WaitingPatient[] {
    if (!this.summary) return [];
    if (!this.searchQuery.trim()) return this.summary.proximosPacientes;
    const q = this.searchQuery.toLowerCase().trim();
    return this.summary.proximosPacientes.filter(
      p => p.patientName.toLowerCase().includes(q) || p.patientId.toString().includes(q)
    );
  }

  atenderPaciente(paciente: WaitingPatient): void {
    this.dashboardService.updateMeetingStatus(paciente.id, 'Completada').subscribe(() => {
      paciente.status = 'Completada';
      if (this.summary) {
        this.summary.pacientesAtendidos++;
      }
      this.router.navigate(['/historias-clinicas', paciente.patientId]);
    });
  }

  verFicha(paciente: WaitingPatient): void {
    this.router.navigate(['/historias-clinicas', paciente.patientId]);
  }

  reabastecer(medicineId: string): void {
    this.restocking = true;
    this.dashboardService.restock(medicineId, 10).subscribe(() => {
      this.restocking = false;
      this.restockSuccess = '¡Reabastecido con éxito (+10 unidades)!';
      if (this.summary && this.summary.alertasStockBajo.length > 0) {
        this.summary.alertasStockBajo[0].currentStock += 10;
        this.summary.stockCritico = Math.max(0, this.summary.stockCritico - 1);
      }
      setTimeout(() => {
        this.restockSuccess = '';
      }, 3000);
    });
  }
}
