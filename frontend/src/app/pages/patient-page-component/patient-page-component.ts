import { Component } from '@angular/core';
import { UserNavbarComponent } from '../../shared/components/user-navbar-component/user-navbar-component';
import { SidebarComponent } from '../../shared/components/sidebar-component/sidebar-component';
import { MenuItem } from '../../core/interface/menu.item.interface';
import { RouterOutlet } from '@angular/router';

@Component({
  imports: [UserNavbarComponent, SidebarComponent, RouterOutlet],
  selector: 'app-patient-page-component',
  styleUrl: './patient-page-component.css',
  templateUrl: './patient-page-component.html',
})
export class PatientPageComponent {

  menuItems: MenuItem[] = [
    {
      label: 'Agendar cita',
      icon: 'calendar',
      route: 'agendar-cita',
    },

    {
      label: 'Historial clínico',
      icon: 'history',
      route: 'historial-clinico',
    }
  ];
}
