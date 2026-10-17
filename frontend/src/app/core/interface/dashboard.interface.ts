export interface WaitingPatient {
  id: string;
  startsAt: string;
  timeFormatted: string;
  patientId: number;
  patientName: string;
  doctorId: number;
  doctorName: string;
  dentalChairName: string;
  status: 'Pendiente' | 'Confirmada' | 'En espera' | 'Completada' | 'Cancelada' | 'No_Asistio' | string;
}

export interface LowStockAlert {
  medicineId: string;
  medicineName: string;
  currentStock: number;
  minimumStock: number;
  unitCost: number;
}

export interface DashboardSummary {
  citasHoy: number;
  pacientesAtendidos: number;
  ingresosEstimados: number;
  stockCritico: number;
  proximosPacientes: WaitingPatient[];
  alertasStockBajo: LowStockAlert[];
}

