import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {
  ClinicalEvaluationRequest,
  ClinicalEvolutionRequest,
  ClinicalHistoryRecord,
  PatientProfile
} from '../interface/patientProfile.interface';

@Injectable({
  providedIn: 'root'
})
export class ClinicalHistoryApiService {
  private http = inject(HttpClient);
  private apiUrl = '/api/clinical-histories';

  private defaultProfile: PatientProfile = {
    patientId: 1094821402,
    name: 'María',
    lastName: 'García Pérez',
    birthDate: '1998-05-14',
    age: 28,
    phone: '+57 312 456 7890',
    email: 'maria.garcia@email.com',
    rh: 'O+',
    lastVisit: '15 de Agosto, 2026',
    medicalAlerts: [
      'Alergia a Penicilina',
      'Hipertensión leve'
    ],
    records: [
      {
        id: 'rec-1',
        recordDate: '15/08/2026',
        toothPiece: '#16',
        procedureName: 'Limpieza Profunda + Detartraje',
        diagnosis: 'Gingivitis localizada en cuadrante superior derecho',
        observations: 'Paciente tolera bien procedimiento.',
        doctorName: 'Dr. Alex González',
        signed: true
      },
      {
        id: 'rec-2',
        recordDate: '02/05/2026',
        toothPiece: '#21',
        procedureName: 'Valoración Inicial + Periodontal',
        diagnosis: 'Sondaje periodontal normal',
        observations: 'Se recomienda placa de relajación nocturna.',
        doctorName: 'Dra. Elena Martínez',
        signed: true
      },
      {
        id: 'rec-3',
        recordDate: '10/01/2026',
        toothPiece: 'General',
        procedureName: 'Profilaxis General y Aplicación de Flúor',
        diagnosis: 'Control preventivo semestral',
        observations: 'Técnica de cepillado reforzada.',
        doctorName: 'Dr. Alex González',
        signed: true
      },
      {
        id: 'rec-4',
        recordDate: '18/11/2025',
        toothPiece: '#36',
        procedureName: 'Obturación en Amalgama (Reemplazo)',
        diagnosis: 'Filtración marginal antigua',
        observations: 'Ajuste oclusal verificado.',
        doctorName: 'Dr. Alex González',
        signed: true
      },
      {
        id: 'rec-5',
        recordDate: '05/09/2025',
        toothPiece: '#46',
        procedureName: 'Exodoncia Simple sin Complicaciones',
        diagnosis: 'Resto radicular',
        observations: 'Sutura reabsorbible, hemostasia lograda.',
        doctorName: 'Dra. Elena Martínez',
        signed: true
      }
    ]
  };

  getPatientProfile(patientId: number | string): Observable<PatientProfile> {
    return this.http.get<PatientProfile>(`${this.apiUrl}/patient/${patientId}`).pipe(
      catchError(() => of(this.defaultProfile))
    );
  }

  addEvolution(req: ClinicalEvolutionRequest): Observable<ClinicalHistoryRecord> {
    return this.http.post<ClinicalHistoryRecord>(`${this.apiUrl}/evolution`, req).pipe(
      catchError(() => {
        const newRecord: ClinicalHistoryRecord = {
          id: 'rec-' + Date.now(),
          recordDate: new Date().toLocaleDateString('es-CO'),
          toothPiece: req.toothPiece,
          procedureName: req.procedureName,
          diagnosis: req.diagnosis,
          observations: req.observations || '',
          doctorName: 'Dr. Alex González',
          signed: true
        };
        this.defaultProfile.records.unshift(newRecord);
        return of(newRecord);
      })
    );
  }

  addEvaluation(req: ClinicalEvaluationRequest): Observable<ClinicalHistoryRecord> {
    return this.http.post<ClinicalHistoryRecord>(`${this.apiUrl}/evaluation`, req).pipe(
      catchError(() => {
        const newRecord: ClinicalHistoryRecord = {
          id: 'eval-' + Date.now(),
          recordDate: new Date().toLocaleDateString('es-CO'),
          toothPiece: 'General',
          procedureName: 'Evaluación / Examen Clínico',
          diagnosis: req.primaryDiagnosis,
          observations: req.suggestedPlan || '',
          doctorName: 'Dr. Alex González',
          signed: true
        };
        this.defaultProfile.records.unshift(newRecord);
        return of(newRecord);
      })
    );
  }
}

