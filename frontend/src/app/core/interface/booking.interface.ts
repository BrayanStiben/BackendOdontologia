export interface DoctorSummary {
  id: number;
  name: string;
  lastName: string;
  specialty: string;
}

export interface PublicSlot {
  startsAt: string;
  endsAt: string;
  timeFormatted: string;
}

export interface PublicBookingRequest {
  patientId: number;
  patientName: string;
  patientLastName: string;
  patientPhone: string;
  patientEmail: string;
  doctorId: number;
  startsAt: string;
  endsAt: string;
  reason?: string;
  receiveWhatsAppNotification?: boolean;
}

export interface PublicBookingResponse {
  meetingId: string;
  confirmationCode: string;
  startsAt: string;
  endsAt: string;
  doctorName: string;
  patientName: string;
  chairName: string;
  status: string;
  message: string;
}

