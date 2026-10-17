export interface BillItem {
  item: number;
  code: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discountPercent?: number;
  discountAmount: number;
  subtotal: number;
}

export interface BillItemRequest {
  cupsCode: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discountPercent: number;
}

export interface BillCreateRequest {
  patientId: number;
  meetingId?: string;
  address?: string;
  paymentMethod?: string;
  observations?: string;
  items: BillItemRequest[];
}

export interface Bill {
  id: string;
  billNumber: string;
  issueDate: string;
  patientId: number;
  patientName: string;
  nit: number | string;
  address: string;
  paymentMethod: string;
  subtotal: number;
  discount: number;
  tax: number;
  totalCost: number;
  totalInWords: string;
  cufe: string;
  dianResolution: string;
  qrData: string;
  observations: string;
  items: BillItem[];
}

