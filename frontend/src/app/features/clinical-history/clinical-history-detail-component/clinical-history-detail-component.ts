import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClinicalHistoryApiService } from '../../../core/service/clinical-history-api.service';
import { PatientProfile, ClinicalHistoryRecord } from '../../../core/interface/patientProfile.interface';

@Component({
  imports: [CommonModule, FormsModule, RouterLink],
  selector: 'app-clinical-history-detail',
  styleUrl: './clinical-history-detail-component.css',
  templateUrl: './clinical-history-detail-component.html',
  standalone: true
})
export class ClinicalHistoryDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private historyApi = inject(ClinicalHistoryApiService);

  patientId = 1094821402;
  profile: PatientProfile | null = null;
  loading = true;

  // Pestaña activa: 'evolucion' (Page 5), 'odontograma' (Page 6 placeholder), 'evaluacion' (Page 7)
  activeTab: 'evolucion' | 'odontograma' | 'evaluacion' = 'evolucion';

  // Modal de RX
  showRxModal = false;
  showEditPatientModal = false;

  // Formulario de Nueva Evolución (Page 5)
  dienteZona = '#16 (Molar)';
  procedimiento = 'Resina de Fotocurado';
  descripcionEvolucion = 'Se realiza cavidad clase I en diente #16. Se coloca grabado acido, adhesivo y resina A2. Oclusion ajustada y pulida sin complicaciones.';
  savingEvolution = false;
  evolutionSuccessMsg = '';

  // Formulario de Evaluación Clínica (Page 7)
  motivoConsulta = 'Dolor al masticar en zona posterior derecha';
  enfermedadActual = 'Paciente refiere molestia desde hace 3 dias, punzante, desencadenada por frio y dulce.';
  pa = '120/80';
  fc = '72 bpm';
  temp = '36.5 C';
  peso = '62 kg';

  atmArticulacion = 'Normal sin chasquidos ni dolor';
  tejidosBlandos = 'Gingivitis marginal leve en zona molar';
  higieneOral = 'Regular';
  indicePlaca = '28%';
  calculoDental = true;
  empaquetamiento = false;

  diagnosticoPrincipal = 'K02.1 - Caries de la dentina (Pieza #16)';
  diagnosticoSecundario = 'K05.1 - Gingivitis cronica';
  pronostico = 'Favorable';
  planSugerido = '1. Detartraje ultrasonico y profilaxis general\n2. Restauracion con resina de fotocurado en pieza #16\n3. Reevaluacion en 15 dias e instruccion de higiene oral.';
  savingEvaluation = false;
  evaluationSuccessMsg = '';

  searchQuery = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.patientId = Number(idParam.replace(/\D/g, '')) || 1094821402;
      }
      this.cargarPerfil();
    });

    // Detectar si la URL termina en /evaluacion
    this.route.url.subscribe(segments => {
      const isEval = segments.some(s => s.path === 'evaluacion');
      if (isEval) {
        this.activeTab = 'evaluacion';
      }
    });
  }

  cargarPerfil(): void {
    this.loading = true;
    this.historyApi.getPatientProfile(this.patientId).subscribe(data => {
      this.profile = data;
      this.loading = false;
    });
  }

  cambiarPestana(tab: 'evolucion' | 'odontograma' | 'evaluacion'): void {
    this.activeTab = tab;
  }

  guardarEvolucion(): void {
    if (!this.descripcionEvolucion.trim()) {
      alert('Por favor ingresa la descripción del procedimiento.');
      return;
    }

    this.savingEvolution = true;
    this.historyApi.addEvolution({
      patientId: this.patientId,
      doctorId: 1,
      toothPiece: this.dienteZona,
      procedureName: this.procedimiento,
      diagnosis: this.procedimiento,
      observations: this.descripcionEvolucion
    }).subscribe(newRecord => {
      this.savingEvolution = false;
      this.evolutionSuccessMsg = '¡Evolución registrada y firmada con éxito!';
      if (this.profile) {
        this.profile.records.unshift(newRecord);
      }
      setTimeout(() => {
        this.evolutionSuccessMsg = '';
      }, 3500);
    });
  }

  guardarEvaluacion(): void {
    this.savingEvaluation = true;
    this.historyApi.addEvaluation({
      patientId: this.patientId,
      doctorId: 1,
      motivoConsulta: this.motivoConsulta,
      enfermedadActual: this.enfermedadActual,
      bloodPressure: this.pa,
      heartRate: this.fc,
      temperature: this.temp,
      weight: this.peso,
      atmArticulation: this.atmArticulacion,
      softTissues: this.tejidosBlandos,
      oralHygiene: this.higieneOral,
      plaqueIndex: this.indicePlaca,
      dentalCalculus: this.calculoDental,
      foodImpaction: this.empaquetamiento,
      primaryDiagnosis: this.diagnosticoPrincipal,
      secondaryDiagnosis: this.diagnosticoSecundario,
      prognosis: this.pronostico,
      suggestedPlan: this.planSugerido
    }).subscribe(newRecord => {
      this.savingEvaluation = false;
      this.evaluationSuccessMsg = '¡Evaluación clínica guardada y firmada digitalmente!';
      if (this.profile) {
        this.profile.records.unshift(newRecord);
      }
      setTimeout(() => {
        this.evaluationSuccessMsg = '';
        this.activeTab = 'evolucion';
      }, 2000);
    });
  }

  cancelarEvaluacion(): void {
    this.activeTab = 'evolucion';
  }

  irAFacturacion(): void {
    this.router.navigate(['/facturacion/nueva'], { queryParams: { paciente: this.patientId } });
  }

  abrirRxModal(): void {
    this.showRxModal = true;
  }

  cerrarRxModal(): void {
    this.showRxModal = false;
  }

  exportarPdf(): void {
    window.print();
  }

  buscarOtroPaciente(): void {
    if (this.searchQuery.trim()) {
      this.router.navigate(['/historias-clinicas', this.searchQuery.trim()]);
    }
  }
}

