import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Bill, BillCreateRequest } from '../interface/bill.interface';

@Injectable({
  providedIn: 'root'
})
export class BillingApiService {
  private http = inject(HttpClient);
  private apiUrl = '/api/bills';

  private defaultBill: Bill = {
    id: 'FE-2026-0492',
    billNumber: 'FE-2026-0492',
    issueDate: '09/09/2026 - 08:30 AM',
    patientId: 1094821402,
    patientName: 'María García Pérez',
    nit: '1094.821.402',
    address: 'Cra 15 #22-10, Armenia',
    paymentMethod: 'Contado - Tarjeta Crédito',
    subtotal: 280000,
    discount: 14000,
    tax: 0,
    totalCost: 266000,
    totalInWords: 'DOSCIENTOS SESENTA Y SEIS MIL PESOS M/CTE.',
    cufe: 'a1b2c3d4e5f67890123456789abcdef0123456789a',
    dianResolution: 'Res. DIAN Nº: 187600012345 de 2026',
    qrData: 'NumFac=FE-2026-0492;Total=266000;CUFE=a1b2c3d4e5f67890123456789abcdef0123456789a',
    observations: 'Tratamiento realizado en sesión única. Incluye garantía de 6 meses sobre resina.',
    items: [
      {
        item: 1,
        code: 'CUPS-890201',
        description: 'Consulta de primera vez por odontología general',
        quantity: 1,
        unitPrice: 60000,
        discountPercent: 0,
        discountAmount: 0,
        subtotal: 60000
      },
      {
        item: 2,
        code: 'CUPS-232101',
        description: 'Resina de fotocurado de alta densidad (#16)',
        quantity: 1,
        unitPrice: 140000,
        discountPercent: 10,
        discountAmount: 14000,
        subtotal: 126000
      },
      {
        item: 3,
        code: 'CUPS-997301',
        description: 'Detartraje y profilaxis dental completa',
        quantity: 1,
        unitPrice: 80000,
        discountPercent: 0,
        discountAmount: 0,
        subtotal: 80000
      }
    ]
  };

  create(req: BillCreateRequest): Observable<Bill> {
    return this.http.post<Bill>(this.apiUrl, req).pipe(
      catchError(() => {
        let gross = 0;
        let disc = 0;
        const items = req.items.map((it, idx) => {
          const lineGross = it.unitPrice * it.quantity;
          const lineDisc = lineGross * ((it.discountPercent || 0) / 100);
          const lineSub = lineGross - lineDisc;
          gross += lineGross;
          disc += lineDisc;
          return {
            item: idx + 1,
            code: it.cupsCode,
            description: it.description,
            quantity: it.quantity,
            unitPrice: it.unitPrice,
            discountPercent: it.discountPercent,
            discountAmount: lineDisc,
            subtotal: lineSub
          };
        });

        const total = gross - disc;
        const generatedBill: Bill = {
          ...this.defaultBill,
          id: 'FE-2026-' + Math.floor(1000 + Math.random() * 9000),
          billNumber: 'FE-2026-' + Math.floor(1000 + Math.random() * 9000),
          issueDate: new Date().toLocaleDateString('es-CO') + ' - ' + new Date().toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit' }),
          patientId: req.patientId,
          address: req.address || 'Cra 15 #22-10, Armenia',
          paymentMethod: req.paymentMethod || 'Tarjeta de Crédito',
          subtotal: gross,
          discount: disc,
          tax: 0,
          totalCost: total,
          observations: req.observations || 'Tratamiento odontológico realizado.',
          items
        };
        this.defaultBill = generatedBill;
        return of(generatedBill);
      })
    );
  }

  get(id: string): Observable<Bill> {
    return this.http.get<Bill>(`${this.apiUrl}/${id}`).pipe(
      catchError(() => of(this.defaultBill))
    );
  }

  getByPatient(patientId: number): Observable<Bill[]> {
    return this.http.get<Bill[]>(`${this.apiUrl}/patient/${patientId}`).pipe(
      catchError(() => of([this.defaultBill]))
    );
  }
}

