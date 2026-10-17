export interface ClinicalHistoryRecord {
  id: string;
  recordDate: string;
  toothPiece: string;
  procedureName: string;
  diagnosis: string;
  observations: string;
  doctorName: string;
  signed: boolean;
}

export interface PatientProfile {
  patientId: number;
  name: string;
  lastName: string;
  birthDate: string;
  phone: string;
  email: string;
  rh?: string;
  age?: number;
  lastVisit?: string;
  medicalAlerts?: string[];
  records: ClinicalHistoryRecord[];
}

export interface ClinicalEvolutionRequest {
  patientId: number;
  doctorId: number;
  toothPiece: string;
  procedureName: string;
  diagnosis: string;
  observations?: string;
}

export interface ClinicalEvaluationRequest {
  patientId: number;
  doctorId: number;
  motivoConsulta: string;
  enfermedadActual: string;
  bloodPressure?: string;
  heartRate?: string;
  temperature?: string;
  weight?: string;
  atmArticulation?: string;
  softTissues?: string;
  oralHygiene?: string;
  plaqueIndex?: string;
  dentalCalculus?: boolean;
  foodImpaction?: boolean;
  primaryDiagnosis: string;
  secondaryDiagnosis?: string;
  prognosis?: string;
  suggestedPlan?: string;
}

