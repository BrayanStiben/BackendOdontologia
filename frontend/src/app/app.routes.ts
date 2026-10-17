import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page-component/home-page-component';
import { LoginComponent } from './features/auth/login-component/login-component';
import { RegisterComponent } from './features/auth/register-component/register-component';
import { PatientPageComponent } from './pages/patient-page-component/patient-page-component';
import { PatientHomeComponent } from './features/patient/patient-home-component/patient-home-component';
import { AppointmentComponent } from './features/patient/appointment-component/appointment-component';
import { ClinicalHistoryComponent } from './features/patient/clinical-history-component/clinical-history-component';
import { DentistPageComponent } from './pages/dentist-page-component/dentist-page-component';
import { DentistHomeComponent } from './features/dentist/dentist-home-component/dentist-home-component';
import { CalendarComponent } from './features/dentist/calendar-component/calendar-component';
import { ConsultationComponent } from './features/dentist/consultation-component/consultation-component';
import { DiagnoseComponent } from './features/dentist/diagnose-component/diagnose-component';
import { ClinicalHistoryDetailComponent } from './features/clinical-history/clinical-history-detail-component/clinical-history-detail-component';
import { InvoiceCreateComponent } from './features/billing/invoice-create-component/invoice-create-component';
import { InvoiceViewComponent } from './features/billing/invoice-view-component/invoice-view-component';

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent,
  },
  // Página 1: Login
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'authentication/login',
    component: LoginComponent,
  },
  // Página 2: Registro
  {
    path: 'registro',
    component: RegisterComponent,
  },
  {
    path: 'authentication/register',
    component: RegisterComponent,
  },
  // Página 3: Panel de Control / Dashboard
  {
    path: 'dashboard',
    component: DentistPageComponent,
    children: [
      {
        path: '',
        component: DentistHomeComponent,
      }
    ]
  },
  // Página 4: Reserva tu Cita Odontológica 24/7
  {
    path: 'reservar',
    component: AppointmentComponent,
  },
  // Página 5 y 7: Historias Clínicas Digitales & Evaluación
  {
    path: 'historias-clinicas',
    component: ClinicalHistoryDetailComponent,
  },
  {
    path: 'historias-clinicas/:id',
    component: ClinicalHistoryDetailComponent,
  },
  {
    path: 'historias-clinicas/:id/evaluacion',
    component: ClinicalHistoryDetailComponent,
  },
  // Página 8: Generación de Factura Electrónica
  {
    path: 'facturacion/nueva',
    component: InvoiceCreateComponent,
  },
  // Página 9: Representación Gráfica de Factura Electrónica
  {
    path: 'facturacion/ver/:id',
    component: InvoiceViewComponent,
  },
  {
    path: 'patient-dashboard',
    component: PatientPageComponent,
    children: [
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full',
      },
      {
        path: 'home',
        component: PatientHomeComponent,
      },
      {
        path: 'agendar-cita',
        component: AppointmentComponent,
      },
      {
        path: 'historial-clinico',
        component: ClinicalHistoryComponent,
      }
    ],
  },
  {
    path: 'dentist-dashboard',
    component: DentistPageComponent,
    children: [
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full',
      },
      {
        path: 'home',
        component: DentistHomeComponent,
      },
      {
        path: 'calendario',
        component: CalendarComponent,
      },
      {
        path: 'consultar',
        component: ConsultationComponent,
      },
      {
        path: 'diagnosticar',
        component: DiagnoseComponent,
      },
      {
        path: 'historias-clinicas',
        component: ClinicalHistoryDetailComponent,
      },
      {
        path: 'facturacion',
        component: InvoiceCreateComponent,
      }
    ],
  },
  {
    path: '**',
    redirectTo: '',
  }
];
