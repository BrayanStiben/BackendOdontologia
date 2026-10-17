export interface Meeting {
  id: number;
  hora: string;
  doctor: string;
  paciente: string;
  fecha: string;
  estado: 'En espera' | 'Confirmado' | 'Pendiente' | 'Agendado' | 'Atendido' | 'Cancelado';
}
