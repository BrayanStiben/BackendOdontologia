import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BillingApiService } from '../../../core/service/billing-api.service';
import { Bill } from '../../../core/interface/bill.interface';

@Component({
  imports: [CommonModule, RouterLink],
  selector: 'app-invoice-view',
  styleUrl: './invoice-view-component.css',
  templateUrl: './invoice-view-component.html',
  standalone: true
})
export class InvoiceViewComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private billingApi = inject(BillingApiService);

  billId = 'FE-2026-0492';
  bill: Bill | null = null;
  loading = true;
  emailSentMessage = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.billId = id;
      }
      this.cargarFactura();
    });
  }

  cargarFactura(): void {
    this.loading = true;
    this.billingApi.get(this.billId).subscribe(data => {
      this.bill = data;
      this.loading = false;
    });
  }

  descargarPdf(): void {
    window.print();
  }

  imprimir(): void {
    window.print();
  }

  enviarPorCorreo(): void {
    const email = 'maria.garcia@email.com';
    this.emailSentMessage = `Factura enviada exitosamente a ${email}`;
    setTimeout(() => {
      this.emailSentMessage = '';
    }, 3500);
  }
}

