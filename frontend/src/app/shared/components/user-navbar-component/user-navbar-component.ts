import { Component, inject} from '@angular/core';
import { SessionService } from '../../../core/service/session.service';

@Component({
  imports: [],
  selector: 'app-user-navbar-component',
  styleUrl: './user-navbar-component.css',
  templateUrl: './user-navbar-component.html',
})
export class UserNavbarComponent {
  private userService = inject(SessionService);
  usuario = this.userService.usuarioActual;

  logout() {}
}
