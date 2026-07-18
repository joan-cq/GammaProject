-- Migración: mejora de categorización de logs (categoria, entidad, usuario responsable)
-- Ejecutar DESPUÉS de haber importado colegio_gamma.sql
-- (y después de migracion_asistencia_anuncio.sql, si ya la aplicaste)

ALTER TABLE `logs`
  CHANGE `level` `categoria` varchar(30) DEFAULT NULL,
  CHANGE `message` `mensaje` text DEFAULT NULL,
  ADD COLUMN `entidad` varchar(50) DEFAULT NULL AFTER `categoria`,
  ADD COLUMN `usuario_dni` varchar(20) DEFAULT NULL AFTER `entidad`;

-- Los registros antiguos quedan con entidad y usuario_dni en NULL; se muestran
-- en el frontend como "—" y siguen apareciendo en la categoría INFO.
