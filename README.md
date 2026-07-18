# Gamma — Sistema de Gestión Académica

Sistema web para el Colegio Gamma: gestión de alumnos, profesores, cursos, notas, asistencia y anuncios institucionales, con un portal de consulta para padres/alumnos.

- **Frontend**: React
- **Backend**: Java 21 + Spring Boot (API REST)
- **Base de datos**: MySQL / MariaDB

Para el detalle técnico completo (arquitectura, endpoints, modelos, sistema de logs) revisa [`DOCUMENTACION.md`](DOCUMENTACION.md). Para el manual de uso pensado para Administradores, Profesores y Padres/Alumnos, revisa `Manual_de_Usuario_Gamma.pdf`.

## Requisitos previos

- **XAMPP** (o cualquier MySQL/MariaDB local) — puerto 3306.
- **Java 21** y **Maven** (o usa el wrapper `./mvnw` incluido).
- **Node.js** y **npm**.

## 1. Base de datos

1. Inicia MySQL desde el panel de XAMPP.
2. En phpMyAdmin, crea la base `colegio_gamma`.
3. Importa, **en este orden**:
   1. `colegio_gamma.sql`
   2. `migracion_asistencia_anuncio.sql`
   3. `migracion_logs.sql`
4. Revisa que el usuario/clave configurados en `gamma-backend/src/main/resources/application.properties` (`spring.datasource.username` / `spring.datasource.password`) coincidan con tu MySQL local.

## 2. Backend

```bash
cd gamma-backend
./mvnw spring-boot:run
```

Queda disponible en `http://localhost:8080`.

## 3. Frontend

```bash
cd gamma
npm install
npm start
```

Queda disponible en `http://localhost:3000`.

## Orden de arranque

1. MySQL (XAMPP)
2. Backend (`mvn`)
3. Frontend (`npm start`)

## Roles del sistema

| Rol | Cómo ingresa | Qué puede hacer |
|---|---|---|
| **Administrador** | `/login` con DNI y clave | Gestiona alumnos, profesores, administradores, cursos, año escolar y ve los logs del sistema. |
| **Profesor** | `/login` con DNI y clave | Registra notas (y las exporta a PDF), registra asistencia y publica anuncios de su curso. |
| **Padre / Alumno** | `/notas` con el DNI del alumno y su clave | Consulta notas, asistencia y anuncios institucionales. |

## Notas importantes

- Los archivos de `gamma/src/recursos/` (imágenes y videos) deben existir con los nombres exactos que referencian los componentes; si faltan, `npm run build` falla. Ver sección 8 de `DOCUMENTACION.md`.
- Los `.log` diarios de auditoría se guardan en la ruta configurada en `app.logs.path` (`application.properties`). Ajústala a tu entorno si no vas a usar `D:/REPOSITORIOS/GammaProject_new/logs`.
- Si cambias el dominio/puerto del backend, actualiza `gamma/src/componentes/api.js` (`baseURL`) y la lista de orígenes CORS en `SecurityConfig.java`.