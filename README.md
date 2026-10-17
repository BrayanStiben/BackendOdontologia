# Doc-Tooth — Sistema Odontológico Integral

Proyecto de gestión odontológica integral que incluye backend en **Spring Boot (Java 21)** y frontend en **Angular 22**.

---

## 📁 Estructura del Repositorio

```text
├── backend/                  # Proyecto Spring Boot (API REST, Seguridad, JPA, Base de datos)
│   ├── src/
│   ├── database/
│   ├── pom.xml
│   └── mvnw
└── frontend/                 # Proyecto Angular 22 (Vistas, Componentes, Servicios, Mockups)
    ├── src/
    ├── public/
    ├── package.json
    └── angular.json
```

---

## 🚀 Cómo Iniciar los Proyectos

### Backend (Spring Boot)
1. Ingresa a la carpeta `backend`:
   ```bash
   cd backend
   ```
2. Ejecuta el servidor con Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   *La API estará escuchando en `http://localhost:8080/`.*

---

### Frontend (Angular)
1. Ingresa a la carpeta `frontend`:
   ```bash
   cd frontend
   ```
2. Instala dependencias e inicia el servidor de desarrollo:
   ```bash
   npm install
   npm start
   ```
   *El frontend estará disponible en `http://localhost:4200/`.*
