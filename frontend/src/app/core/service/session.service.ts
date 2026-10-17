
import { Injectable, signal } from '@angular/core';

export interface Usuario {
  id: number;
  nombre: string;
  correo: string;
  phone: string;
}

@Injectable({
  providedIn: 'root', // Singleton: una sola instancia para toda la app
})
export class SessionService {
  // Usuario de prueba "quemado" por ahora
  private _usuarioActual = signal<Usuario>({
    id: 1,
    nombre: 'Juan Sebastian Gonzalez Mancera',
    correo: 'jsebastian@gmail.com',
    phone: '+57 3105448342',
  });

  // Exponemos solo lectura hacia afuera
  usuarioActual = this._usuarioActual.asReadonly();

  // Método para actualizarlo más adelante (ej. al hacer login real)
  actualizarUsuario(usuario: Usuario) {
    this._usuarioActual.set(usuario);
  }
}
