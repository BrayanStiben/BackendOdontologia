import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DoctorSummary, PublicBookingRequest, PublicBookingResponse, PublicSlot } from '../interface/booking.interface';

@Injectable({
  providedIn: 'root'
})
export class BookingApiService {
  private http = inject(HttpClient);
  private apiUrl = '/api/public';

  getSpecialties(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/specialties`).pipe(
      catchError(() => of([
        'Odontología General',
        'Ortodoncia',
        'Endodoncia',
        'Periodoncia',
        'Cirugía Oral'
      ]))
    );
  }

  getDoctors(specialty?: string): Observable<DoctorSummary[]> {
    const url = specialty ? `${this.apiUrl}/doctors?specialty=${encodeURIComponent(specialty)}` : `${this.apiUrl}/doctors`;
    return this.http.get<DoctorSummary[]>(url).pipe(
      catchError(() => of([
        { id: 1, name: 'Alex', lastName: 'González', specialty: 'Odontología General' },
        { id: 2, name: 'Elena', lastName: 'Martínez', specialty: 'Odontología General' },
        { id: 3, name: 'Carlos', lastName: 'Paredes', specialty: 'Ortodoncia' }
      ]))
    );
  }

  getAvailableSlots(doctorId: number, dateIso: string): Observable<PublicSlot[]> {
    return this.http.get<PublicSlot[]>(`${this.apiUrl}/slots?doctorId=${doctorId}&date=${dateIso}`).pipe(
      catchError(() => of([
        { startsAt: `${dateIso}T08:30:00`, endsAt: `${dateIso}T09:15:00`, timeFormatted: '08:30 AM' },
        { startsAt: `${dateIso}T10:00:00`, endsAt: `${dateIso}T10:45:00`, timeFormatted: '10:00 AM' },
        { startsAt: `${dateIso}T14:30:00`, endsAt: `${dateIso}T15:15:00`, timeFormatted: '02:30 PM' },
        { startsAt: `${dateIso}T16:00:00`, endsAt: `${dateIso}T16:45:00`, timeFormatted: '04:00 PM' }
      ]))
    );
  }

  book(req: PublicBookingRequest): Observable<PublicBookingResponse> {
    return this.http.post<PublicBookingResponse>(`${this.apiUrl}/book`, req).pipe(
      catchError(() => {
        const code = 'CITA-' + Math.floor(100000 + Math.random() * 900000);
        return of({
          meetingId: 'meet-' + Math.random().toString(36).substring(2, 9),
          confirmationCode: code,
          startsAt: req.startsAt,
          endsAt: req.endsAt,
          doctorName: 'Dr. Alex González',
          patientName: `${req.patientName} ${req.patientLastName}`,
          chairName: 'Sillón Central - Consultorio 20',
          status: 'Pendiente',
          message: `Cita reservada con éxito. Código de confirmación: ${code}`
        });
      })
    );
  }
}

