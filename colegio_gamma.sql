-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-07-2025 a las 05:25:13
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `colegio_gamma`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrador`
--

CREATE TABLE `administrador` (
  `dni` varchar(8) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `celular` varchar(255) DEFAULT NULL,
  `id_anio` int(10) UNSIGNED NOT NULL,
  `estado` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `administrador`
--

INSERT INTO `administrador` (`dni`, `nombre`, `apellido`, `celular`, `id_anio`, `estado`) VALUES
('12345678', 'Joan', 'Cristobal', '987654321', 1, 'ACTIVO'),
('22222222', 'Milagros', 'Torres', '999999999', 2, 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumno`
--

CREATE TABLE `alumno` (
  `dni` varchar(8) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `celular_apoderado` varchar(255) DEFAULT NULL,
  `genero` varchar(255) DEFAULT NULL,
  `codigo_grado` varchar(10) DEFAULT NULL,
  `id_anio` int(10) UNSIGNED NOT NULL,
  `estado` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alumno`
--

INSERT INTO `alumno` (`dni`, `nombre`, `apellido`, `celular_apoderado`, `genero`, `codigo_grado`, `id_anio`, `estado`) VALUES
('44444444', 'Patricia', 'Solano', '966666666', 'FEMENINO', '1PRI', 1, 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `anio_escolar`
--

CREATE TABLE `anio_escolar` (
  `id_anio` int(10) UNSIGNED NOT NULL,
  `anio` int(11) NOT NULL,
  `estado` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `anio_escolar`
--

INSERT INTO `anio_escolar` (`id_anio`, `anio`, `estado`) VALUES
(1, 2025, 'ACTIVO'),
(2, 2026, 'INACTIVO'),
(3, 2027, 'INACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bimestre`
--

CREATE TABLE `bimestre` (
  `id_bimestre` int(10) UNSIGNED NOT NULL,
  `id_anio` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `bimestre`
--

INSERT INTO `bimestre` (`id_bimestre`, `id_anio`, `nombre`, `fecha_inicio`, `fecha_fin`) VALUES
(1, 1, 'PRIMER BIMESTRE', '2025-03-15', '2025-05-21'),
(2, 1, 'SEGUNDO BIMESTRE', '2025-05-22', '2025-07-28'),
(3, 1, 'TERCER BIMESTRE', '2025-07-29', '2025-10-04'),
(4, 1, 'CUARTO BIMESTRE', '2025-10-05', '2025-12-15');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `curso`
--

CREATE TABLE `curso` (
  `codigo_curso` varchar(10) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `estado` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `curso`
--

INSERT INTO `curso` (`codigo_curso`, `nombre`, `estado`) VALUES
('ART', 'ARTE', 'ACTIVO'),
('CIN', 'CIENCIAS NATURALES', 'ACTIVO'),
('CIS', 'CIENCIAS SOCIALES', 'ACTIVO'),
('COM', 'COMUNICACION', 'ACTIVO'),
('DEP', 'DESARROLLO PERSONAL', 'ACTIVO'),
('EDC', 'EDUCACION CIVICA', 'ACTIVO'),
('EDF', 'EDUCACION FISICA', 'ACTIVO'),
('ING', 'INGLES', 'ACTIVO'),
('MAT', 'MATEMATICA', 'ACTIVO'),
('REL', 'RELIGION', 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grado`
--

CREATE TABLE `grado` (
  `codigo_grado` varchar(10) NOT NULL,
  `nombre_grado` varchar(255) DEFAULT NULL,
  `nivel` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `grado`
--

INSERT INTO `grado` (`codigo_grado`, `nombre_grado`, `nivel`) VALUES
('1PRI', '1', 'PRIMARIA'),
('1SEC', '1', 'SECUNDARIA'),
('2PRI', '2', 'PRIMARIA'),
('2SEC', '2', 'SECUNDARIA'),
('3PRI', '3', 'PRIMARIA'),
('3SEC', '3', 'SECUNDARIA'),
('4PRI', '4', 'PRIMARIA'),
('4SEC', '4', 'SECUNDARIA'),
('5PRI', '5', 'PRIMARIA'),
('5SEC', '5', 'SECUNDARIA'),
('6PRI', '6', 'PRIMARIA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grado_curso`
--

CREATE TABLE `grado_curso` (
  `codigo_grado` varchar(10) NOT NULL,
  `codigo_curso` varchar(10) NOT NULL,
  `id_anio` int(10) UNSIGNED NOT NULL,
  `estado` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `grado_curso`
--

INSERT INTO `grado_curso` (`codigo_grado`, `codigo_curso`, `id_anio`, `estado`) VALUES
('1PRI', 'CIN', 1, 'ACTIVO'),
('1PRI', 'CIS', 1, 'ACTIVO'),
('1PRI', 'COM', 1, 'ACTIVO'),
('1PRI', 'EDF', 1, 'ACTIVO'),
('1PRI', 'MAT', 1, 'ACTIVO'),
('1PRI', 'REL', 1, 'ACTIVO'),
('1SEC', 'ART', 1, 'ACTIVO'),
('1SEC', 'CIN', 1, 'ACTIVO'),
('1SEC', 'CIS', 1, 'ACTIVO'),
('1SEC', 'COM', 1, 'ACTIVO'),
('1SEC', 'DEP', 1, 'ACTIVO'),
('1SEC', 'EDC', 1, 'ACTIVO'),
('1SEC', 'EDF', 1, 'ACTIVO'),
('1SEC', 'ING', 1, 'ACTIVO'),
('1SEC', 'MAT', 1, 'ACTIVO'),
('1SEC', 'REL', 1, 'ACTIVO'),
('2PRI', 'CIN', 1, 'ACTIVO'),
('2PRI', 'CIS', 1, 'ACTIVO'),
('2PRI', 'COM', 1, 'ACTIVO'),
('2PRI', 'EDF', 1, 'ACTIVO'),
('2PRI', 'MAT', 1, 'ACTIVO'),
('2PRI', 'REL', 1, 'ACTIVO'),
('2SEC', 'ART', 1, 'ACTIVO'),
('2SEC', 'CIN', 1, 'ACTIVO'),
('2SEC', 'CIS', 1, 'ACTIVO'),
('2SEC', 'COM', 1, 'ACTIVO'),
('2SEC', 'DEP', 1, 'ACTIVO'),
('2SEC', 'EDC', 1, 'ACTIVO'),
('2SEC', 'EDF', 1, 'ACTIVO'),
('2SEC', 'ING', 1, 'ACTIVO'),
('2SEC', 'MAT', 1, 'ACTIVO'),
('2SEC', 'REL', 1, 'ACTIVO'),
('3PRI', 'CIN', 1, 'ACTIVO'),
('3PRI', 'CIS', 1, 'ACTIVO'),
('3PRI', 'COM', 1, 'ACTIVO'),
('3PRI', 'EDF', 1, 'ACTIVO'),
('3PRI', 'MAT', 1, 'ACTIVO'),
('3PRI', 'REL', 1, 'ACTIVO'),
('3SEC', 'ART', 1, 'ACTIVO'),
('3SEC', 'CIN', 1, 'ACTIVO'),
('3SEC', 'CIS', 1, 'ACTIVO'),
('3SEC', 'COM', 1, 'ACTIVO'),
('3SEC', 'DEP', 1, 'ACTIVO'),
('3SEC', 'EDC', 1, 'ACTIVO'),
('3SEC', 'EDF', 1, 'ACTIVO'),
('3SEC', 'ING', 1, 'ACTIVO'),
('3SEC', 'MAT', 1, 'ACTIVO'),
('3SEC', 'REL', 1, 'ACTIVO'),
('4PRI', 'CIN', 1, 'ACTIVO'),
('4PRI', 'CIS', 1, 'ACTIVO'),
('4PRI', 'COM', 1, 'ACTIVO'),
('4PRI', 'EDF', 1, 'ACTIVO'),
('4PRI', 'MAT', 1, 'ACTIVO'),
('4PRI', 'REL', 1, 'ACTIVO'),
('4SEC', 'ART', 1, 'ACTIVO'),
('4SEC', 'CIN', 1, 'ACTIVO'),
('4SEC', 'CIS', 1, 'ACTIVO'),
('4SEC', 'COM', 1, 'ACTIVO'),
('4SEC', 'DEP', 1, 'ACTIVO'),
('4SEC', 'EDC', 1, 'ACTIVO'),
('4SEC', 'EDF', 1, 'ACTIVO'),
('4SEC', 'ING', 1, 'ACTIVO'),
('4SEC', 'MAT', 1, 'ACTIVO'),
('4SEC', 'REL', 1, 'ACTIVO'),
('5PRI', 'CIN', 1, 'ACTIVO'),
('5PRI', 'CIS', 1, 'ACTIVO'),
('5PRI', 'COM', 1, 'ACTIVO'),
('5PRI', 'EDF', 1, 'ACTIVO'),
('5PRI', 'MAT', 1, 'ACTIVO'),
('5PRI', 'REL', 1, 'ACTIVO'),
('5SEC', 'ART', 1, 'ACTIVO'),
('5SEC', 'CIN', 1, 'ACTIVO'),
('5SEC', 'CIS', 1, 'ACTIVO'),
('5SEC', 'COM', 1, 'ACTIVO'),
('5SEC', 'DEP', 1, 'ACTIVO'),
('5SEC', 'EDC', 1, 'ACTIVO'),
('5SEC', 'EDF', 1, 'ACTIVO'),
('5SEC', 'ING', 1, 'ACTIVO'),
('5SEC', 'MAT', 1, 'ACTIVO'),
('5SEC', 'REL', 1, 'ACTIVO'),
('6PRI', 'CIN', 1, 'ACTIVO'),
('6PRI', 'CIS', 1, 'ACTIVO'),
('6PRI', 'COM', 1, 'ACTIVO'),
('6PRI', 'EDF', 1, 'ACTIVO'),
('6PRI', 'MAT', 1, 'ACTIVO'),
('6PRI', 'REL', 1, 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `logs`
--

CREATE TABLE `logs` (
  `id` bigint(20) NOT NULL,
  `level` varchar(50) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `logs`
--

INSERT INTO `logs` (`id`, `level`, `message`, `timestamp`) VALUES
(1, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 01:35:41'),
(2, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 01:35:41'),
(3, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 01:37:00'),
(4, 'INFO', 'Se ha listado los administradores.', '2025-07-21 01:37:08'),
(5, 'INFO', 'Se ha agregado un nuevo administrador con DNI: 56655222', '2025-07-21 01:38:45'),
(6, 'INFO', 'Se ha listado los administradores.', '2025-07-21 01:38:45'),
(7, 'INFO', 'Se ha listado los administradores.', '2025-07-21 01:39:11'),
(8, 'INFO', 'Se ha eliminado el administrador con DNI: 56655222', '2025-07-21 01:39:14'),
(9, 'INFO', 'Se ha listado los administradores.', '2025-07-21 01:39:14'),
(10, 'INFO', 'Se ha listado los administradores.', '2025-07-21 01:40:07'),
(11, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 01:40:42'),
(12, 'INFO', 'Se ha listado los cursos.', '2025-07-21 01:40:43'),
(13, 'INFO', 'Se ha listado los administradores.', '2025-07-21 01:40:44'),
(14, 'INFO', 'Se ha listado los cursos.', '2025-07-21 01:40:45'),
(16, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 01:40:46'),
(17, 'WARN', 'Intento de inicio de sesión fallido para el usuario con DNI: 12345678', '2025-07-21 01:43:31'),
(18, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 01:43:38'),
(19, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 01:43:38'),
(20, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 01:44:41'),
(21, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 01:49:43'),
(22, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 01:49:46'),
(23, 'INFO', 'Se ha listado los cursos.', '2025-07-21 01:51:31'),
(24, 'INFO', 'Se ha listado los cursos.', '2025-07-21 01:51:39'),
(25, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 01:53:23'),
(26, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 01:53:30'),
(27, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 01:54:00'),
(28, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 44444444', '2025-07-21 01:55:46'),
(29, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 01:55:46'),
(30, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 01:55:46'),
(31, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 01:56:00'),
(32, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 2', '2025-07-21 01:56:02'),
(33, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 01:56:07'),
(34, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 2', '2025-07-21 01:56:07'),
(35, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 3', '2025-07-21 01:56:10'),
(36, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 01:56:31'),
(37, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 01:56:31'),
(38, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 33333333', '2025-07-21 02:04:17'),
(39, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 02:04:23'),
(40, 'INFO', 'Se ha actualizado la nota con ID: 1', '2025-07-21 02:04:27'),
(41, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 02:04:27'),
(42, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:04:34'),
(43, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:04:34'),
(44, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:14:40'),
(45, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:14:40'),
(46, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:14:43'),
(47, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:14:44'),
(48, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:14:44'),
(49, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:14:46'),
(51, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:14:48'),
(53, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:14:49'),
(54, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:15:43'),
(56, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:15:43'),
(57, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:15:44'),
(59, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:15:45'),
(60, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:15:46'),
(61, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:15:47'),
(62, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:15:48'),
(63, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:15:51'),
(64, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:16:10'),
(66, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:16:10'),
(67, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:16:13'),
(69, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:16:28'),
(70, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:16:28'),
(71, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:16:31'),
(72, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:16:32'),
(74, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:16:33'),
(76, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:16:35'),
(77, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 33333333', '2025-07-21 02:16:50'),
(78, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:16:59'),
(79, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:16:59'),
(80, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:17:02'),
(82, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:22:56'),
(84, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:23:02'),
(85, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:23:02'),
(86, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:23:04'),
(88, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:28:35'),
(89, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:28:35'),
(90, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:28:37'),
(92, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:28:42'),
(93, 'INFO', 'Se ha actualizado el estado del año escolar 2026 a ACTIVO', '2025-07-21 02:28:44'),
(94, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:28:44'),
(96, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:28:47'),
(97, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:28:48'),
(98, 'INFO', 'Se ha actualizado el estado del año escolar 2026 a INACTIVO', '2025-07-21 02:28:49'),
(99, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:28:49'),
(100, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:28:51'),
(101, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:28:53'),
(103, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:30:05'),
(104, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:30:06'),
(106, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:30:17'),
(107, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:30:25'),
(109, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:37:08'),
(110, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:37:08'),
(111, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:38:18'),
(112, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:38:20'),
(113, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:38:32'),
(114, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:38:34'),
(115, 'INFO', 'Se ha listado los profesores.', '2025-07-21 02:38:34'),
(116, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:39:20'),
(117, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:39:21'),
(118, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:39:22'),
(119, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:39:24'),
(120, 'INFO', 'Se ha listado los profesores.', '2025-07-21 02:39:24'),
(121, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:39:36'),
(122, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:39:49'),
(123, 'INFO', 'Se ha listado los profesores.', '2025-07-21 02:39:49'),
(124, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:40:59'),
(125, 'INFO', 'Se ha listado los profesores.', '2025-07-21 02:40:59'),
(126, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 33333333', '2025-07-21 02:41:19'),
(127, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 02:41:25'),
(128, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:41:33'),
(129, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:41:33'),
(130, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:41:51'),
(131, 'INFO', 'Se ha listado los profesores.', '2025-07-21 02:41:51'),
(132, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:41:58'),
(133, 'INFO', 'Se ha listado los profesores.', '2025-07-21 02:41:58'),
(134, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:42:14'),
(135, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:42:15'),
(137, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:49:44'),
(139, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:49:45'),
(140, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:49:46'),
(142, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:49:52'),
(144, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 02:53:41'),
(145, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:53:42'),
(147, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:55:31'),
(149, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:57:27'),
(151, 'INFO', 'Se ha listado los administradores.', '2025-07-21 02:57:29'),
(153, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:57:30'),
(154, 'WARN', 'Intento de inicio de sesión fallido para el usuario con DNI: 12345678', '2025-07-21 02:57:46'),
(155, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 02:57:52'),
(156, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 02:57:52'),
(157, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:57:55'),
(159, 'INFO', 'Se ha listado los cursos.', '2025-07-21 02:58:02'),
(161, 'INFO', 'Se ha agregado un nuevo profesor con DNI: 34234234', '2025-07-21 03:01:34'),
(163, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:03:03'),
(164, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:03:04'),
(173, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:12:24'),
(175, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:19:54'),
(176, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:19:55'),
(178, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:19:57'),
(179, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:19:57'),
(181, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 03:20:20'),
(182, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:20:20'),
(183, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:20:22'),
(186, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:27:21'),
(187, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:27:23'),
(188, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:27:23'),
(189, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:27:25'),
(191, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:27:25'),
(192, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:27:26'),
(199, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:33:04'),
(201, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:33:04'),
(202, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:33:05'),
(204, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:33:06'),
(205, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:34:44'),
(206, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:34:50'),
(208, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:35:32'),
(209, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:35:33'),
(211, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:36:45'),
(212, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:36:47'),
(214, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:42:19'),
(215, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:42:20'),
(216, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:42:20'),
(217, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:42:25'),
(218, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:42:26'),
(219, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:42:26'),
(220, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:42:27'),
(221, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:42:28'),
(222, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:42:30'),
(223, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:42:46'),
(224, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:42:46'),
(225, 'INFO', 'Se ha eliminado el profesor con DNI: 34234234', '2025-07-21 03:42:51'),
(226, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:42:51'),
(227, 'INFO', 'Se ha actualizado el profesor con DNI: 33333333', '2025-07-21 03:42:56'),
(228, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:42:56'),
(229, 'INFO', 'Se ha actualizado el profesor con DNI: 33333333', '2025-07-21 03:43:02'),
(230, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:43:02'),
(231, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:43:06'),
(232, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:44:32'),
(233, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:44:33'),
(234, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:44:34'),
(235, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:44:34'),
(236, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:44:34'),
(237, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:44:36'),
(238, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:44:36'),
(239, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:44:36'),
(240, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:44:37'),
(241, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:44:37'),
(242, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:45:01'),
(243, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:45:01'),
(244, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:45:03'),
(245, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:45:04'),
(246, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:45:05'),
(247, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:45:06'),
(248, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:45:09'),
(249, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:45:09'),
(250, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:45:13'),
(251, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:45:14'),
(252, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:48:04'),
(253, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:48:07'),
(254, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:48:07'),
(255, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:48:11'),
(256, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:48:16'),
(257, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:48:16'),
(258, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:48:16'),
(259, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:48:17'),
(260, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 03:57:52'),
(261, 'INFO', 'Se ha listado los administradores.', '2025-07-21 03:57:54'),
(262, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:57:55'),
(263, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 03:57:55'),
(264, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:57:59'),
(265, 'INFO', 'Se ha listado los cursos.', '2025-07-21 03:58:56'),
(266, 'INFO', 'Se ha listado los profesores.', '2025-07-21 03:58:56'),
(267, 'INFO', 'Se ha listado los cursos.', '2025-07-21 04:09:54'),
(268, 'INFO', 'Se ha listado los profesores.', '2025-07-21 04:09:54'),
(269, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 44444444', '2025-07-21 04:37:42'),
(270, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 04:37:42'),
(271, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 04:37:42'),
(272, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 04:38:20'),
(273, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 04:38:28'),
(274, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 04:38:28'),
(275, 'INFO', 'Se ha listado los cursos.', '2025-07-21 04:38:32'),
(276, 'INFO', 'Se ha listado los administradores.', '2025-07-21 04:38:34'),
(277, 'INFO', 'Se ha listado los cursos.', '2025-07-21 04:38:35'),
(278, 'INFO', 'Se ha listado los profesores.', '2025-07-21 04:38:35'),
(279, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 04:38:36'),
(280, 'INFO', 'Se ha listado los administradores.', '2025-07-21 04:39:01'),
(281, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 33333333', '2025-07-21 04:39:14'),
(282, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 04:39:19'),
(283, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 44444444', '2025-07-21 04:41:24'),
(284, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 04:41:24'),
(285, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 04:41:24'),
(286, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 04:41:28'),
(287, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 2', '2025-07-21 04:41:30'),
(288, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso REL y bimestre 1', '2025-07-21 04:41:33'),
(289, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso REL y bimestre 2', '2025-07-21 04:41:34'),
(290, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 04:41:48'),
(291, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 04:41:48'),
(292, 'INFO', 'Se ha listado los cursos.', '2025-07-21 04:41:51'),
(293, 'INFO', 'Se ha listado los administradores.', '2025-07-21 04:41:52'),
(294, 'INFO', 'Se ha listado los cursos.', '2025-07-21 04:41:52'),
(295, 'INFO', 'Se ha listado los profesores.', '2025-07-21 04:41:52'),
(296, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 04:41:53'),
(297, 'INFO', 'Se ha listado los cursos.', '2025-07-21 04:42:18'),
(298, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 33333333', '2025-07-21 04:42:28'),
(299, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 2', '2025-07-21 04:42:34'),
(300, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 12345678', '2025-07-21 06:21:20'),
(301, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 06:21:20'),
(302, 'INFO', 'Se ha listado los cursos.', '2025-07-21 06:21:23'),
(303, 'INFO', 'Se ha listado los administradores.', '2025-07-21 06:21:23'),
(304, 'INFO', 'Se ha listado los cursos.', '2025-07-21 06:21:24'),
(305, 'INFO', 'Se ha listado los profesores.', '2025-07-21 06:21:24'),
(306, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 06:21:24'),
(307, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 33333333', '2025-07-21 06:21:34'),
(308, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 06:21:38'),
(309, 'INFO', 'Inicio de sesión exitoso para el usuario con DNI: 44444444', '2025-07-21 06:21:45'),
(310, 'INFO', 'Se ha listado los alumnos.', '2025-07-21 06:21:45'),
(311, 'INFO', 'Se ha listado los años escolares.', '2025-07-21 06:21:45'),
(312, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso REL y bimestre 1', '2025-07-21 06:21:51'),
(313, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso REL y bimestre 2', '2025-07-21 06:21:52'),
(314, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 1', '2025-07-21 06:21:54'),
(315, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 2', '2025-07-21 06:21:54'),
(316, 'INFO', 'Se han listado las notas de los alumnos del grado 1PRI para el curso COM y bimestre 3', '2025-07-21 06:21:55');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `nota`
--

CREATE TABLE `nota` (
  `id_nota` bigint(20) NOT NULL,
  `dni_alumno` varchar(8) NOT NULL,
  `codigo_curso` varchar(10) NOT NULL,
  `id_bimestre` int(10) UNSIGNED NOT NULL,
  `nota` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `nota`
--

INSERT INTO `nota` (`id_nota`, `dni_alumno`, `codigo_curso`, `id_bimestre`, `nota`) VALUES
(1, '44444444', 'COM', 1, 12),
(4, '44444444', 'COM', 2, 18),
(6, '44444444', 'REL', 1, 15),
(7, '44444444', 'COM', 3, 12);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profesor`
--

CREATE TABLE `profesor` (
  `dni` varchar(8) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `celular` varchar(255) DEFAULT NULL,
  `codigo_curso` varchar(10) DEFAULT NULL,
  `id_anio` int(10) UNSIGNED NOT NULL,
  `estado` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `profesor`
--

INSERT INTO `profesor` (`dni`, `nombre`, `apellido`, `celular`, `codigo_curso`, `id_anio`, `estado`) VALUES
('33333333', 'Rodney', 'Ruiz', '977777777', 'COM', 1, 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `dni` varchar(8) NOT NULL,
  `clave` varchar(255) NOT NULL,
  `rol` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`dni`, `clave`, `rol`) VALUES
('12345678', '$2a$12$z/n0IqstLCDGAZ7gJ7edmegK.MwNiTmfKSyEnFefUbV7ZB1WmWL4y', 'ADMINISTRADOR'),
('22222222', '$2a$12$yNzFFkaBRe./BiMaTPsHN.mPL2ukaJLRG2JzjsGLc8buHwz/txbFK', 'ADMINISTRADOR'),
('33333333', '$2a$12$lOYoWxr8hvCRJV0vM.amOe0GGcTPjMzYLaJHZiig1P5UD.ncubK6m', 'PROFESOR'),
('44444444', '$2a$12$PL6i2fQQTJf8VOrg3cttMu037gqwdcEj4K9l6.aGgty5yb5ClA9T6', 'ALUMNO');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD PRIMARY KEY (`dni`),
  ADD KEY `id_anio` (`id_anio`);

--
-- Indices de la tabla `alumno`
--
ALTER TABLE `alumno`
  ADD PRIMARY KEY (`dni`),
  ADD KEY `codigo_grado` (`codigo_grado`),
  ADD KEY `id_anio` (`id_anio`);

--
-- Indices de la tabla `anio_escolar`
--
ALTER TABLE `anio_escolar`
  ADD PRIMARY KEY (`id_anio`),
  ADD UNIQUE KEY `anio` (`anio`);

--
-- Indices de la tabla `bimestre`
--
ALTER TABLE `bimestre`
  ADD PRIMARY KEY (`id_bimestre`),
  ADD KEY `id_anio` (`id_anio`);

--
-- Indices de la tabla `curso`
--
ALTER TABLE `curso`
  ADD PRIMARY KEY (`codigo_curso`);

--
-- Indices de la tabla `grado`
--
ALTER TABLE `grado`
  ADD PRIMARY KEY (`codigo_grado`);

--
-- Indices de la tabla `grado_curso`
--
ALTER TABLE `grado_curso`
  ADD PRIMARY KEY (`codigo_grado`,`codigo_curso`,`id_anio`),
  ADD KEY `codigo_curso` (`codigo_curso`),
  ADD KEY `id_anio` (`id_anio`);

--
-- Indices de la tabla `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `nota`
--
ALTER TABLE `nota`
  ADD PRIMARY KEY (`id_nota`),
  ADD KEY `dni_alumno` (`dni_alumno`),
  ADD KEY `codigo_curso` (`codigo_curso`),
  ADD KEY `id_bimestre` (`id_bimestre`);

--
-- Indices de la tabla `profesor`
--
ALTER TABLE `profesor`
  ADD PRIMARY KEY (`dni`),
  ADD KEY `codigo_curso` (`codigo_curso`),
  ADD KEY `id_anio` (`id_anio`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `anio_escolar`
--
ALTER TABLE `anio_escolar`
  MODIFY `id_anio` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `bimestre`
--
ALTER TABLE `bimestre`
  MODIFY `id_bimestre` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `logs`
--
ALTER TABLE `logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=317;

--
-- AUTO_INCREMENT de la tabla `nota`
--
ALTER TABLE `nota`
  MODIFY `id_nota` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD CONSTRAINT `administrador_ibfk_1` FOREIGN KEY (`dni`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE,
  ADD CONSTRAINT `administrador_ibfk_2` FOREIGN KEY (`id_anio`) REFERENCES `anio_escolar` (`id_anio`) ON DELETE CASCADE;

--
-- Filtros para la tabla `alumno`
--
ALTER TABLE `alumno`
  ADD CONSTRAINT `alumno_ibfk_1` FOREIGN KEY (`dni`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE,
  ADD CONSTRAINT `alumno_ibfk_2` FOREIGN KEY (`codigo_grado`) REFERENCES `grado` (`codigo_grado`) ON DELETE SET NULL,
  ADD CONSTRAINT `alumno_ibfk_3` FOREIGN KEY (`id_anio`) REFERENCES `anio_escolar` (`id_anio`) ON DELETE CASCADE;

--
-- Filtros para la tabla `bimestre`
--
ALTER TABLE `bimestre`
  ADD CONSTRAINT `bimestre_ibfk_1` FOREIGN KEY (`id_anio`) REFERENCES `anio_escolar` (`id_anio`) ON DELETE CASCADE;

--
-- Filtros para la tabla `grado_curso`
--
ALTER TABLE `grado_curso`
  ADD CONSTRAINT `grado_curso_ibfk_1` FOREIGN KEY (`codigo_grado`) REFERENCES `grado` (`codigo_grado`) ON DELETE CASCADE,
  ADD CONSTRAINT `grado_curso_ibfk_2` FOREIGN KEY (`codigo_curso`) REFERENCES `curso` (`codigo_curso`) ON DELETE CASCADE,
  ADD CONSTRAINT `grado_curso_ibfk_3` FOREIGN KEY (`id_anio`) REFERENCES `anio_escolar` (`id_anio`) ON DELETE CASCADE;

--
-- Filtros para la tabla `nota`
--
ALTER TABLE `nota`
  ADD CONSTRAINT `nota_ibfk_1` FOREIGN KEY (`dni_alumno`) REFERENCES `alumno` (`dni`) ON DELETE CASCADE,
  ADD CONSTRAINT `nota_ibfk_2` FOREIGN KEY (`codigo_curso`) REFERENCES `curso` (`codigo_curso`) ON DELETE CASCADE,
  ADD CONSTRAINT `nota_ibfk_3` FOREIGN KEY (`id_bimestre`) REFERENCES `bimestre` (`id_bimestre`) ON DELETE CASCADE;

--
-- Filtros para la tabla `profesor`
--
ALTER TABLE `profesor`
  ADD CONSTRAINT `profesor_ibfk_1` FOREIGN KEY (`dni`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE,
  ADD CONSTRAINT `profesor_ibfk_2` FOREIGN KEY (`codigo_curso`) REFERENCES `curso` (`codigo_curso`) ON DELETE SET NULL,
  ADD CONSTRAINT `profesor_ibfk_3` FOREIGN KEY (`id_anio`) REFERENCES `anio_escolar` (`id_anio`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
