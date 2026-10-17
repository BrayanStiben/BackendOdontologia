import { Component, Input } from '@angular/core';
import { MenuItem } from '../../../core/interface/menu.item.interface';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  imports: [RouterLink, RouterLinkActive],
  selector: 'app-sidebar-component',
  styleUrl: './sidebar-component.css',
  templateUrl: './sidebar-component.html',
  standalone: true,
})
export class SidebarComponent {
  @Input()
  menuItems: MenuItem[] = [];
}
