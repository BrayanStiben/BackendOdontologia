import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DashboardSummary } from '../interface/dashboard.interface';

@Injectable({
  providedIn: 'root'
})
export class DashboardApiService {
  private http = inject(HttpClient);
  private apiUrl = '/api/dashboard';

  private defaultMock: DashboardSummary = {
    citasHoy: 12,
    pacientesAtendidos: 8,
    ingresosEstimados: 680.0,
    stockCritico: 1,
    proximosPacientes: [
      {
        id: '1',
        startsAt: new Date().toISOString(),
        timeFormatted: '08:30 AM',
        patientId: 1094821402,
        patientName: 'María García',
        doctorId: 1,
        doctorName: 'Dr. González',
        dentalChairName: 'Sillón Central 1',
        status: 'En espera'
      },
      {
        id: '2',
        startsAt: new Date().toISOString(),
        timeFormatted: '10:00 AM',
        patientId: 1094821403,
        patientName: 'Luis Sánchez',
        doctorId: 1,
        doctorName: 'Dr. González',
        dentalChairName: 'Sillón Central 2',
        status: 'Confirmado'
      },
      {
        id: '3',
        startsAt: new Date().toISOString(),
        timeFormatted: '11:30 AM',
        patientId: 1094821404,
        patientName: 'Ana Ramírez',
        doctorId: 2,
        doctorName: 'Dra. Martínez',
        dentalChairName: 'Sillón Lateral 1',
        status: 'Confirmado'
      },
      {
        id: '4',
        startsAt: new Date().toISOString(),
        timeFormatted: '02:00 PM',
        patientId: 1094821405,
        patientName: 'Carlos Gómez',
        doctorId: 1,
        doctorName: 'Dr. González',
        dentalChairName: 'Sillón Central 1',
        status: 'Pendiente'
      },
      {
        id: '5',
        startsAt: new Date().toISOString(),
        timeFormatted: '03:30 PM',
        patientId: 1094821406,
        patientName: 'Laura Patiño',
        doctorId: 2,
        doctorName: 'Dra. Martínez',
        dentalChairName: 'Sillón Lateral 2',
        status: 'Agendado'
      }
    ],
    alertasStockBajo: [
      {
        medicineId: 'resina-z350-4g',
        medicineName: 'Resina Z350 (4g)',
        currentStock: 2,
        minimumStock: 5,
        unitCost: 45000
      }
    ]
  };

  getSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.apiUrl}/summary`).pipe(
      catchError(() => of(this.defaultMock))
    );
  }

  updateMeetingStatus(meetingId: string, status: string): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(`${this.apiUrl}/meetings/${meetingId}/status?status=${status}`, {}).pipe(
      catchError(() => of({ message: `Estado actualizado a ${status}` }))
    );
  }

  restock(medicineId: string, quantity: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/restock`, { medicineId, quantity }).pipe(
      catchError(() => of({ success: true, message: 'Inventario reabastecido con éxito' }))
    );
  }
}

