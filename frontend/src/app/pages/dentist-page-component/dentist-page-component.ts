import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../../shared/components/sidebar-component/sidebar-component';
import { UserNavbarComponent } from '../../shared/components/user-navbar-component/user-navbar-component';
import { MenuItem } from '../../core/interface/menu.item.interface';

@Component({
  imports: [RouterOutlet, SidebarComponent, UserNavbarComponent],
  selector: 'app-dentist-page-component',
  styleUrl: './dentist-page-component.css',
  templateUrl: './dentist-page-component.html',
})
export class DentistPageComponent {
  menuItems: MenuItem[] = [
    {
      label: 'Agenda Diaria',
      icon: 'calendar_month',
      route: 'calendario',
    },
    {
      label: 'Historias Clínicas',
      icon: 'medical_services',
      route: 'historias-clinicas',
    },
    {
      label: 'Facturación',
      icon: 'receipt',
      route: 'facturacion',
    },
    {
      label: 'Inventario',
      icon: 'inventory',
      route: 'inventario',
    },
    {
      label: 'Configuración',
      icon: 'settings',
      route: 'configuracion',
    },
  ];
}
