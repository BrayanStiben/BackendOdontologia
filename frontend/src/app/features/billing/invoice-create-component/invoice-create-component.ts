import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BillingApiService } from '../../../core/service/billing-api.service';
import { BillItemRequest } from '../../../core/interface/bill.interface';

interface EditableBillItem {
  cupsCode: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discountPercent: number;
}

@Component({
  imports: [CommonModule, FormsModule, RouterLink],
  selector: 'app-invoice-create',
  styleUrl: './invoice-create-component.css',
  templateUrl: './invoice-create-component.html',
  standalone: true
})
export class InvoiceCreateComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private billingApi = inject(BillingApiService);

  patientId = 1094821402;

  // Datos Sección 1
  nombreRazon = 'María García Pérez';
  documentoNit = '1094.821.402';
  fechaEmision = '2026-09-09';
  direccion = 'Cra 15 #22-10, Armenia';
  metodoPago = 'Tarjeta de Crédito';
  nroFactura = 'FE-2026-0492';

  // Datos Sección 2
  items: EditableBillItem[] = [
    {
      cupsCode: 'CUPS-890201',
      description: 'Consulta de primera vez por odontología general',
      quantity: 1,
      unitPrice: 60000,
      discountPercent: 0
    },
    {
      cupsCode: 'CUPS-232101',
      description: 'Resina de fotocurado de alta densidad (Pieza #16)',
      quantity: 1,
      unitPrice: 140000,
      discountPercent: 10
    },
    {
      cupsCode: 'CUPS-997301',
      description: 'Detartraje y profilaxis dental completa',
      quantity: 1,
      unitPrice: 80000,
      discountPercent: 0
    }
  ];

  // Datos Sección 3
  observaciones = 'Tratamiento realizado en sesión única. Incluye garantía de 6 meses sobre resina.';

  // Modal para agregar procedimiento
  showAddModal = false;
  newCups = 'CUPS-890202';
  newDesc = 'Control odontológico periódico';
  newPrice = 50000;
  newQty = 1;
  newDisc = 0;

  isSubmitting = false;

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const p = params.get('paciente');
      if (p) {
        this.patientId = Number(p.replace(/\D/g, '')) || 1094821402;
      }
    });
  }

  getItemSubtotal(item: EditableBillItem): number {
    const gross = item.unitPrice * item.quantity;
    const disc = gross * (item.discountPercent / 100);
    return gross - disc;
  }

  get subtotalBruto(): number {
    return this.items.reduce((sum, it) => sum + (it.unitPrice * it.quantity), 0);
  }

  get totalDescuento(): number {
    return this.items.reduce((sum, it) => sum + ((it.unitPrice * it.quantity) * (it.discountPercent / 100)), 0);
  }

  get totalAPagar(): number {
    return this.subtotalBruto - this.totalDescuento;
  }

  eliminarItem(index: number): void {
    if (this.items.length > 1) {
      this.items.splice(index, 1);
    } else {
      alert('La factura debe tener al menos un concepto.');
    }
  }

  abrirModalAgregar(): void {
    this.showAddModal = true;
  }

  cerrarModalAgregar(): void {
    this.showAddModal = false;
  }

  confirmarAgregar(): void {
    if (!this.newDesc.trim()) {
      alert('Ingresa la descripción del procedimiento.');
      return;
    }
    this.items.push({
      cupsCode: this.newCups,
      description: this.newDesc,
      quantity: this.newQty,
      unitPrice: this.newPrice,
      discountPercent: this.newDisc
    });
    this.showAddModal = false;
  }

  emitirFactura(): void {
    this.isSubmitting = true;
    const reqItems: BillItemRequest[] = this.items.map(it => ({
      cupsCode: it.cupsCode,
      description: it.description,
      quantity: it.quantity,
      unitPrice: it.unitPrice,
      discountPercent: it.discountPercent
    }));

    this.billingApi.create({
      patientId: this.patientId,
      address: this.direccion,
      paymentMethod: this.metodoPago,
      observations: this.observaciones,
      items: reqItems
    }).subscribe(bill => {
      this.isSubmitting = false;
      this.router.navigate(['/facturacion/ver', bill.id || this.nroFactura]);
    });
  }

  imprimirPreFactura(): void {
    window.print();
  }

  cancelar(): void {
    this.router.navigate(['/historias-clinicas', this.patientId]);
  }
}

