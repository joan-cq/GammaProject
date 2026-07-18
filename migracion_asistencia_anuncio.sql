-- Migración: módulos de Asistencia (RF-02 / RF-05) y Anuncios (RF-03 / RF-06)
-- Ejecutar DESPUÉS de haber importado colegio_gamma.sql

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencia`
--

CREATE TABLE `asistencia` (
  `id_asistencia` bigint(20) NOT NULL AUTO_INCREMENT,
  `dni_alumno` varchar(8) NOT NULL,
  `codigo_curso` varchar(10) NOT NULL,
  `fecha` date NOT NULL,
  `estado` varchar(20) NOT NULL, -- PRESENTE, AUSENTE, TARDANZA, JUSTIFICADO
  PRIMARY KEY (`id_asistencia`),
  UNIQUE KEY `uq_asistencia` (`dni_alumno`,`codigo_curso`,`fecha`),
  KEY `codigo_curso` (`codigo_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE `asistencia`
  ADD CONSTRAINT `asistencia_ibfk_1` FOREIGN KEY (`dni_alumno`) REFERENCES `alumno` (`dni`) ON DELETE CASCADE,
  ADD CONSTRAINT `asistencia_ibfk_2` FOREIGN KEY (`codigo_curso`) REFERENCES `curso` (`codigo_curso`) ON DELETE CASCADE;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `anuncio`
--

CREATE TABLE `anuncio` (
  `id_anuncio` bigint(20) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(150) NOT NULL,
  `contenido` text NOT NULL,
  `dni_autor` varchar(8) NOT NULL,
  `nombre_autor` varchar(255) DEFAULT NULL,
  `fecha_publicacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_anuncio`),
  KEY `dni_autor` (`dni_autor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE `anuncio`
  ADD CONSTRAINT `anuncio_ibfk_1` FOREIGN KEY (`dni_autor`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE;
