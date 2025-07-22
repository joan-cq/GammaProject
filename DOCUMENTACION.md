# Documentación del Proyecto Gamma

## 1. Introducción

Este documento describe la arquitectura y estructura del proyecto Gamma, un sistema de gestión académica diseñado para colegios. El sistema consta de un frontend desarrollado en React y un backend en Java con Spring Boot.

## 2. Estructura del Proyecto

El repositorio está organizado en dos carpetas principales:

- **`gamma/`**: Contiene el código fuente del frontend (aplicación de React).
- **`gamma-backend/`**: Contiene el código fuente del backend (API REST con Spring Boot).

---

## 3. Frontend (`gamma/`)

El frontend está construido con React. La estructura de carpetas principal es:

- **`public/`**: Archivos estáticos como `index.html` y assets.
- **`src/`**: Código fuente de la aplicación.
    - **`componentes/`**: Componentes de React que conforman la interfaz de usuario.
    - **`estilos/`**: Hojas de estilo CSS para los componentes.
    - **`recursos/`**: Imágenes, videos y otros recursos multimedia.

### 3.1. Componentes Principales (`src/componentes/`)

- [`ComponenteAdmin.jsx`](gamma/src/componentes/ComponenteAdmin.jsx): Panel de control para administradores.
- [`ComponenteAlumnos.jsx`](gamma/src/componentes/ComponenteAlumnos.jsx): Interfaz para la gestión de alumnos.
- [`ComponenteAnioEscolar.jsx`](gamma/src/componentes/ComponenteAnioEscolar.jsx): Gestión del año escolar.
- [`ComponenteContacto.jsx`](gamma/src/componentes/ComponenteContacto.jsx): Página de contacto.
- [`ComponenteCursos.jsx`](gamma/src/componentes/ComponenteCursos.jsx): Gestión de cursos.
- [`ComponenteInicio.jsx`](gamma/src/componentes/ComponenteInicio.jsx): Página de inicio.
- [`ComponenteLogin.jsx`](gamma/src/componentes/ComponenteLogin.jsx): Formulario de inicio de sesión y lógica de autenticación.
- [`ComponenteLogros.jsx`](gamma/src/componentes/ComponenteLogros.jsx): Sección para mostrar los logros de la institución.
- [`ComponenteLogs.jsx`](gamma/src/componentes/ComponenteLogs.jsx): Visualización de logs del sistema.
- [`ComponenteNosotros.jsx`](gamma/src/componentes/ComponenteNosotros.jsx): Página "Sobre Nosotros".
- [`ComponenteNotas.jsx`](gamma/src/componentes/ComponenteNotas.jsx): Interfaz para la gestión de notas de los alumnos.
- [`ComponentePanelAdmin.jsx`](gamma/src/componentes/ComponentePanelAdmin.jsx): Panel principal del administrador.
- [`ComponentePanelProfesores.jsx`](gamma/src/componentes/ComponentePanelProfesores.jsx): Panel principal para profesores.
- [`ComponenteProfesores.jsx`](gamma/src/componentes/ComponenteProfesores.jsx): Interfaz para la gestión de profesores.
- [`ComponenteProNotas.jsx`](gamma/src/componentes/ComponenteProNotas.jsx): Interfaz para que los profesores gestionen las notas.
- [`ComponenteUpdatePassword.jsx`](gamma/src/componentes/ComponenteUpdatePassword.jsx): Formulario para actualizar la contraseña.
- [`api.js`](gamma/src/componentes/api.js): Módulo para centralizar las llamadas a la API del backend.

---

## 4. Backend (`gamma-backend/`)

El backend es una API RESTful desarrollada con Java y Spring Boot. Sigue una arquitectura de 3 capas: Controladores, Servicios y Repositorios.

### 4.1. Controladores (`src/main/java/com/gamma/backend/controller/`)

Los controladores gestionan las peticiones HTTP y definen los endpoints de la API.

- [`AdminController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/AdminController.java): Endpoints para la gestión de administradores.
- [`AlumnoController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/AlumnoController.java): Endpoints para la gestión de alumnos.
- [`AnioEscolarController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/AnioEscolarController.java): Endpoints para la gestión del año escolar.
- [`AuthController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/AuthController.java): Endpoints para la autenticación de usuarios (login).
- [`BimestreController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/BimestreController.java): Endpoints para la gestión de bimestres.
- [`CursoController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/CursoController.java): Endpoints para la gestión de cursos.
- [`GradoController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/GradoController.java): Endpoints para la gestión de grados.
- [`GradoCursoController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/GradoCursoController.java): Endpoints para la asignación de cursos a grados.
- [`LogController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/LogController.java): Endpoints para la gestión de logs.
- [`NotaController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/NotaController.java): Endpoints para la gestión de notas.
- [`ProfesorController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/ProfesorController.java): Endpoints para la gestión de profesores.
- [`UserController.java`](gamma-backend/src/main/java/com/gamma/backend/controller/UserController.java): Endpoints para la gestión de usuarios en general.

### 4.2. Modelos (`src/main/java/com/gamma/backend/model/`)

Las clases de modelo representan las entidades de la base de datos.

- `Administrador.java`, `Alumno.java`, `Profesor.java`, `User.java`: Entidades para los diferentes tipos de usuarios.
- `AnioEscolar.java`, `Bimestre.java`, `Curso.java`, `Grado.java`, `Nota.java`: Entidades para la estructura académica y calificaciones.
- `Log.java`: Entidad para los registros de logs.

### 4.3. Repositorios (`src/main/java/com/gamma/backend/repository/`)

Interfaces que extienden de `JpaRepository` para interactuar con la base de datos.

### 4.4. Servicios (`src/main/java/com/gamma/backend/service/`)

Contienen la lógica de negocio de la aplicación.

- **`auth/`**: Clases relacionadas con la autenticación y autorización (`LoginService`, `UserDetailsServiceImpl`).
- **`impl/`**: Implementaciones de las interfaces de servicio.
- **`modelservice/`**: Interfaces de los servicios para cada modelo.

## 5. Configuración y Seguridad

- **`SecurityConfig.java`**: Configuración de Spring Security para proteger los endpoints.
- **`JwtRequestFilter.java`**: Filtro para validar los JSON Web Tokens (JWT) en cada petición.
- **`application.properties`**: Archivo de configuración principal, incluyendo la conexión a la base de datos.

## 6. Cómo ejecutar el proyecto

### Backend
1. Navegar a la carpeta `gamma-backend`.
2. Ejecutar `mvn spring-boot:run`.
3. El servidor se iniciará en el puerto 8080.

### Frontend
1. Navegar a la carpeta `gamma`.
2. Ejecutar `npm install` para instalar las dependencias.
3. Ejecutar `npm start` para iniciar el servidor de desarrollo.
4. Abrir `http://localhost:3000` en el navegador.