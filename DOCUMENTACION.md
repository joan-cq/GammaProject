# Documentación del Proyecto Gamma

> Última actualización: incluye los módulos de Asistencia, Anuncios, Exportación de Notas a PDF y el sistema de logs categorizados.

## 1. Introducción

Gamma es un sistema de gestión académica para colegios. Permite administrar alumnos, profesores, cursos y notas; registrar asistencia; publicar anuncios institucionales; y ofrece un portal de consulta para padres/alumnos. El sistema consta de un frontend en **React** y un backend en **Java 21 + Spring Boot**, con **MySQL/MariaDB** como base de datos.

## 2. Estructura del Proyecto

```
GammaProject/
├── gamma/                          # Frontend (React)
├── gamma-backend/                  # Backend (Spring Boot)
├── colegio_gamma.sql               # Dump base de la base de datos
├── migracion_asistencia_anuncio.sql# Migración: tablas asistencia y anuncio
├── migracion_logs.sql              # Migración: columnas categoria/entidad/usuario_dni en logs
├── DOCUMENTACION.md                # Este documento
└── README.md                       # Guía rápida de instalación y ejecución
```

---

## 3. Frontend (`gamma/`)

- **`public/`**: `index.html` y assets estáticos.
- **`src/`**
  - **`componentes/`**: componentes de React.
  - **`estilos/`**: hojas de estilo CSS.
  - **`recursos/`**: imágenes y videos usados por los componentes (ver sección 8, "Recursos multimedia").

### 3.1. Componentes (`src/componentes/`)

| Componente | Rol de acceso | Descripción |
|---|---|---|
| `ComponenteInicio.jsx` | Público | Página de inicio del sitio institucional. |
| `ComponenteNosotros.jsx` | Público | Página "Sobre Nosotros". |
| `ComponenteLogros.jsx` | Público | Logros/hitos de la institución. |
| `ComponenteContacto.jsx` | Público | Formulario/datos de contacto. |
| `ComponenteLogin.jsx` | Público | Login de **Administrador** y **Profesor**. |
| `ComponenteNotas.jsx` | Alumno/Padre | Portal de Padres: login por DNI del alumno; consulta de notas, **asistencia** y **anuncios** (RF-01/02/03). |
| `ComponentePanelAdmin.jsx` | Administrador | Barra de navegación del panel de administración. |
| `ComponenteAlumnos.jsx` | Administrador | CRUD de alumnos. |
| `ComponenteProfesores.jsx` | Administrador | CRUD de profesores (incluye asignación de curso). |
| `ComponenteAdmin.jsx` | Administrador | CRUD de administradores. |
| `ComponenteCursos.jsx` | Administrador | CRUD de cursos. |
| `ComponenteAnioEscolar.jsx` | Administrador | Activar/desactivar años escolares. |
| `ComponenteLogs.jsx` | Administrador | Visualización y filtrado de logs del sistema (ver sección 7). |
| `ComponenteUpdatePassword.jsx` | Administrador | Modal para cambiar la contraseña de un usuario. |
| `ComponentePanelProfesores.jsx` | Profesor | Barra de navegación del panel docente; muestra el curso que dicta. |
| `ComponenteProNotas.jsx` | Profesor | Registro de notas por grado/curso/bimestre + **exportar a PDF** (RF-11). |
| `ComponenteProAsistencia.jsx` | Profesor | Registro diario de asistencia por grado/curso/fecha (RF-05). |
| `ComponenteProAnuncios.jsx` | Profesor | Publicar/eliminar anuncios institucionales (RF-06). |
| `api.js` | — | Instancia de Axios centralizada (`baseURL` + interceptor que agrega el `Authorization: Bearer <token>`). |

### 3.2. Autenticación en el frontend (`App.js`)

- `AuthContext` guarda en `localStorage` (clave `usuario`) el objeto devuelto por el login: `{ token, rol, dni, codigoCurso?, nombreCurso? }`.
- `RutaProtegida` bloquea el acceso a `/panel/**` si no hay sesión iniciada.
- El **nombre del curso del profesor** (`nombreCurso`) se muestra como una etiqueta en la cabecera de `ComponentePanelProfesores.jsx`.

---

## 4. Backend (`gamma-backend/`)

Arquitectura de 3 capas: **Controladores → Servicios → Repositorios**, con JPA/Hibernate sobre MySQL/MariaDB.

### 4.1. Controladores (`controller/`)

| Controlador | Endpoints principales | Notas |
|---|---|---|
| `AuthController` | `POST /api/auth/login` | Devuelve `token`, `rol`, `dni` y, si es profesor, `codigoCurso` y `nombreCurso`. Errores de credenciales devuelven **JSON** (`{"error": "..."}`), no texto plano. |
| `AlumnoController` | `/alumno/list`, `/alumno/add`, `/alumno/update`, `/alumno/delete/{dni}` | |
| `ProfesorController` | `/profesor/list`, `/profesor/add`, `/profesor/update`, `/profesor/delete/{dni}` | |
| `AdminController` | `/administrador/list`, `/administrador/add`, `/administrador/update`, `/administrador/delete/{dni}` | |
| `CursoController` | `/curso/list`, `/curso/add`, `/curso/update/{codigo}`, `/curso/toggle/{codigo}` | |
| `GradoController`, `GradoCursoController`, `BimestreController`, `AnioEscolarController` | CRUD/listados de la estructura académica | |
| `NotaController` | `/notas/alumnos`, `/notas/alumno/{dni}`, `/nota/add`, `/nota/update`, **`/notas/exportar`** | El último genera un PDF con Apache PDFBox (RF-11). |
| `AsistenciaController` **(nuevo)** | `GET /asistencia/alumnos`, `POST /asistencia/registrar`, `GET /asistencia/alumno/{dni}` | RF-02 / RF-05. |
| `AnuncioController` **(nuevo)** | `GET /anuncios`, `POST /anuncios/add`, `DELETE /anuncios/delete/{id}` | RF-03 / RF-06. |
| `LogController` | `GET /logs` | Devuelve todos los logs (`categoria`, `entidad`, `usuarioDni`, `mensaje`, `timestamp`), ordenados del más reciente al más antiguo. |
| `UserController` | Gestión genérica de credenciales/usuario | |

### 4.2. Modelos (`model/`)

- **Usuarios**: `User` (tabla `usuario`: dni, clave, rol), `Administrador`, `Alumno`, `Profesor`.
- **Estructura académica**: `AnioEscolar`, `Bimestre`, `Grado`, `Curso`, `GradoCurso` (+ `GradoCursoId`), `Nota`.
- **Asistencia** **(nuevo)**: `Asistencia` — `alumno`, `curso`, `fecha`, `estado` (`PRESENTE`/`AUSENTE`/`TARDANZA`/`JUSTIFICADO`).
- **Anuncios** **(nuevo)**: `Anuncio` — `titulo`, `contenido`, `dniAutor`, `nombreAutor`, `fechaPublicacion`.
- **Auditoría**: `Log` — `categoria`, `entidad`, `usuarioDni`, `mensaje`, `timestamp` (ver sección 7).

### 4.3. Repositorios (`repository/`)

Interfaces `JpaRepository` para cada entidad. Los más relevantes para los módulos nuevos:
- `AsistenciaRepository`: `findByAlumno_DniAndCurso_CodigoCursoAndFecha`, `findAllByAlumno_DniOrderByFechaDesc`.
- `AnuncioRepository`: `findAllByOrderByFechaPublicacionDesc`.
- `LogRepository`: `findAllByOrderByTimestampDesc`.

### 4.4. Servicios (`service/`)

- **`auth/`**: `LoginService`, `UserDetailsServiceImpl`, `AdminAuthStrategy`, `ProfesorAuthStrategy` (estrategia por rol).
- **`impl/` / `modelservice/`**: lógica de negocio por entidad (p. ej. `NotaService`).
- **`LogService`**: registro de auditoría (ver sección 7).
- **`PdfExportService`** **(nuevo)**: genera el PDF de notas con Apache PDFBox (usado por `NotaController.exportarNotasPDF`).

---

## 5. Configuración y Seguridad

| Archivo | Qué define | Se puede cambiar |
|---|---|---|
| `application.properties` | Conexión a BD, secreto JWT, ruta de logs | Ver sección 6 y 7. |
| `SecurityConfig.java` | Reglas de acceso por endpoint y **CORS** | La lista de orígenes permitidos (`http://localhost:3000`, dominios de producción) se define en `corsConfigurationSource()`. Si despliegas el frontend en otro dominio, agrégalo ahí. |
| `JwtRequestFilter.java` | Valida el JWT de cada petición y puebla el `SecurityContext` (usado también por `LogService` para saber "quién" hizo la acción) | — |
| `gamma/src/componentes/api.js` | `baseURL` del backend para el frontend | Actualmente `http://localhost:8080`. Cámbialo si despliegas el backend en otra URL. |

⚠️ Anteriormente existía una **segunda configuración de CORS** (un `WebMvcConfigurer` en `GammaBackendApplication.java` con `allowedOrigins("*")`) que entraba en conflicto con `SecurityConfig` y causaba que el navegador bloqueara las respuestas del backend aunque este las procesara correctamente. Se eliminó; `SecurityConfig` es ahora la única fuente de verdad para CORS.

---

## 6. Cómo ejecutar el proyecto

### 6.1. Base de datos (XAMPP / MySQL-MariaDB)
1. Levanta MySQL desde XAMPP.
2. En phpMyAdmin, crea la base `colegio_gamma` e importa, **en este orden**:
   1. `colegio_gamma.sql`
   2. `migracion_asistencia_anuncio.sql`
   3. `migracion_logs.sql`
3. Verifica que el usuario/clave de `spring.datasource.username` / `spring.datasource.password` en `application.properties` coincidan con tu MySQL local.

### 6.2. Backend
```bash
cd gamma-backend
./mvnw spring-boot:run
```
Levanta en `http://localhost:8080`.

### 6.3. Frontend
```bash
cd gamma
npm install
npm start
```
Levanta en `http://localhost:3000`.

### 6.4. Orden recomendado
MySQL (XAMPP) → Backend (`mvn`) → Frontend (`npm start`).

---

## 7. Sistema de Logs / Auditoría

Cada acción relevante del sistema (crear, modificar, eliminar, iniciar sesión) queda registrada con:

- **`categoria`**: `INFO` (consultas/listados), `CREACION`, `MODIFICACION`, `ELIMINACION`, `LOGIN`, `ERROR`.
- **`entidad`**: módulo afectado (`ALUMNO`, `PROFESOR`, `ADMINISTRADOR`, `CURSO`, `NOTA`, `ASISTENCIA`, `ANUNCIO`, `ANIO_ESCOLAR`, `AUTH`).
- **`usuarioDni`**: DNI del usuario autenticado que realizó la acción (se obtiene automáticamente del token JWT vía `SecurityContextHolder`; en el login se pasa explícitamente porque aún no hay sesión activa en ese instante).
- **`mensaje`** y **`timestamp`**.

Se guarda en **dos lugares**:
1. **Base de datos** (tabla `logs`), consumida por `GET /logs` y mostrada en `ComponenteLogs.jsx` con filtros por categoría/módulo y buscador por usuario o texto.
2. **Archivo de texto por día**, con rotación automática por fecha: `gamma-yyyy-MM-dd.log`.

La ruta del archivo es configurable en `application.properties`:
```properties
app.logs.path=D:/REPOSITORIOS/GammaProject_new/logs
```
Cámbiala según el entorno donde se despliegue (por ejemplo, una ruta Linux si el backend corre en un servidor). Si la carpeta no existe, se crea automáticamente al primer log del día.

Formato de cada línea del archivo:
```
2026-07-18 17:20:33 | CREACION     | ALUMNO          | usuario=87654321  | Se agregó el alumno con DNI: 12345678
```

> Nota de seguridad: el endpoint `GET /logs` está marcado como público (`permitAll`) en `SecurityConfig`. Si se requiere restringir la auditoría solo a administradores, hay que moverlo a la lista de endpoints autenticados y filtrar por rol.

---

## 8. Recursos multimedia (`gamma/src/recursos/`)

Varios componentes referencian imágenes/videos por nombre exacto (`insignia.jpg`, `video1.mp4`, fotos de alumnos, etc.). Si faltan, `npm run build` falla con `Module not found`. La lista completa de archivos esperados y su componente de origen está documentada como comentario al inicio de cada componente que los usa (`ComponenteInicio.jsx`, `ComponenteLogin.jsx`, `ComponenteNotas.jsx`, `ComponenteNosotros.jsx`). Reemplaza los archivos vacíos de esa carpeta por las imágenes/videos reales del colegio.

---

## 9. Matriz de requisitos funcionales (estado actual)

| Requisito | Descripción | Estado |
|---|---|---|
| RF-01 | Portal de Padres: login por DNI y consulta de notas | ✅ Implementado |
| RF-02 | Portal de Padres: historial de asistencia | ✅ Implementado |
| RF-03 | Portal de Padres: anuncios institucionales | ✅ Implementado |
| RF-05 | Docente registra asistencia diaria | ✅ Implementado |
| RF-06 | Docente publica anuncios | ✅ Implementado |
| RF-11 | Exportar notas a PDF | ✅ Implementado (reporte de una página por grado/curso/bimestre) |
| RF-12 | Backups automáticos de la base de datos | ✅ Implementado (cada 15 dias) |

## 10. Decisiones de diseño y cosas fácilmente configurables

- **Puerto del backend**: 8080 por defecto (Spring Boot). Cambiar con `server.port` en `application.properties`.
- **Secreto JWT** (`jwt.secret`) y expiración (1 día, hardcodeado en `AuthController`): rotarlo en producción.
- **Orígenes CORS permitidos**: `SecurityConfig.corsConfigurationSource()`.
- **Ruta de los `.log` diarios**: `app.logs.path` en `application.properties`.
- **URL del backend que usa el frontend**: `gamma/src/componentes/api.js` (`baseURL`).
- **Estados de asistencia**: actualmente `PRESENTE`, `AUSENTE`, `TARDANZA`, `JUSTIFICADO` (constante `ESTADOS` en `ComponenteProAsistencia.jsx` + validación libre en el backend). Agregar un estado nuevo solo requiere tocar esa constante en el frontend.