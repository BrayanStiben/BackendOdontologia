# Doc-Tooth — Frontend Odontología Integral (AgendasDT)

Sistema Frontend desarrollado en **Angular 22** para la gestión clínica, agendamiento 24/7, historia clínica digital y facturación electrónica de Doc-Tooth.

---

## 🚀 Requisitos y Comandos para Probar en Local

### 1. Instalación de Dependencias
Asegúrate de estar ubicado en la carpeta del frontend (`AgendasDT-Frontend`):
```bash
cd "AgendasDT-Frontend"
npm install
```

### 2. Iniciar el Servidor de Desarrollo Local
Para levantar el aplicativo en local ejecuta:
```bash
npm start
# o alternativamente:
npx ng serve
```

Una vez iniciado, abre tu navegador en:
👉 **[http://localhost:4200/](http://localhost:4200/)**

### 3. Compilación para Producción (Build)
Para comprobar que el proyecto compila y empaqueta sin ningún error:
```bash
npm run build
```

---

## 🧭 Guía de Rutas para Probar las Nuevas Ventanas

Cada pantalla implementada corresponde directamente a los mockups arquitectónicos suministrados:

| Mockup / Página | URL en Local | Descripción y Funcionalidad |
| :--- | :--- | :--- |
| **Página 1: Login** | `http://localhost:4200/login` | Pantalla de inicio de sesión con campo unificado usuario/correo (`dr.gonzalez@doc-tooth.com`), contraseña, recordar sesión y ventana modal de recuperación de contraseña (*¿Olvidaste tu contraseña?*). |
| **Página 2: Registro** | `http://localhost:4200/registro` | Formulario de creación de cuenta en 2 columnas con todos los campos del diseño: *Rol en la Clínica*, *Tarjeta Profesional (TP)*, *Nombre de Clínica / Consultorio*, *Términos de Servicio* y *Habeas Data*. |
| **Página 3: Panel de Control (Dashboard)** | `http://localhost:4200/dashboard` | Dashboard del odontólogo/administrador con las **4 métricas del día** (Citas para Hoy, Pacientes Atendidos, Ingresos Estimados, Stock Crítico), tabla de **Próximos Pacientes en Espera** con acciones `[Atender]` y `[Ver Ficha]`, panel de **Acciones Rápidas** y alerta de **Stock Bajo** con botón funcional de `Reabastecer`. |
| **Página 4: Reserva de Citas 24/7** | `http://localhost:4200/reservar` | Flujo de auto-agendamiento en 3 pasos: (1) Selección de especialidad y doctor, (2) Mini-calendario y selector de horarios disponibles (`08:30 AM`, `10:00 AM`, etc.), (3) Datos del paciente con WhatsApp y panel de resumen con confirmación y código de cita. |
| **Página 5: Historia Clínica (Evolución)** | `http://localhost:4200/historias-clinicas/1094821402` | Ficha lateral del paciente con datos demográficos, **Alertas Médicas** (Alergia a Penicilina, Hipertensión leve), visor de Radiografías RX, formulario de nueva evolución (#16 Molar) y tabla de historial clínico con insignias de firma. |
| **Página 6: Odontograma** | `http://localhost:4200/historias-clinicas/1094821402` *(Pestaña Odontograma)* | Módulo excluido deliberadamente por directriz técnica; contiene aviso informativo y navegación de retorno. |
| **Página 7: Evaluación / Examen Clínico** | `http://localhost:4200/historias-clinicas/1094821402/evaluacion` | Formulario clínico integral: (1) Motivo de consulta y signos vitales (PA, FC, Temp, Peso), (2) Examen estomatológico (ATM, tejidos blandos, higiene, placa), (3) Diagnósticos CIE-10 (K02.1, K05.1), pronóstico y plan sugerido con botón `Guardar y Firmar Evaluación`. |
| **Página 8: Facturación Electrónica** | `http://localhost:4200/facturacion/nueva` | Creación de factura electrónica con datos del cliente, tabla de conceptos CUPS editables (cálculo en tiempo real de subtotales y descuentos), botón `+ Agregar Procedimiento` y botón `Emitir Factura Electrónica (DIAN)`. |
| **Página 9: Representación Gráfica Factura** | `http://localhost:4200/facturacion/ver/FE-2026-0492` | Representación impresa y visual con membrete de la clínica, resolución DIAN, desglose de ítems, valor en letras, código hash CUFE, código QR y botones funcionales de `Descargar PDF`, `Imprimir` y `Enviar por Correo`. |

---

## 🎨 Metodología y Lineamientos de Estilo Aplicados

1. **Control de Flujo Moderno**: Se utilizó exclusivamente `@if`, `@else` y `@for (...; track ...)` de Angular, garantizando **cero uso de directivas obsoletas `*ngIf` y `*ngFor`**.
2. **Paleta y Tipografía**: Se aplicó la paleta oficial del proyecto (`#305B73`, `#5A85A9`, `#6D98BA`, fondos `#f7fafc` y `#EAF4F9`), garantizando armonía visual con los componentes existentes.
3. **Servicios Desacoplados con Fallback**: Los servicios en `src/app/core/service/` implementan comunicación HTTP con el backend Spring Boot y datos por defecto (fallback) para que todas las pantallas funcionen de forma interactiva en local aun si la base de datos o el backend no están en ejecución.
4. **Integridad del Backend**: No se alteró ningún archivo del proyecto backend.
