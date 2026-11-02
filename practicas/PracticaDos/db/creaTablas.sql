/*
  Script de creación de base de datos para Práctica #2 - Consultas Ampliadas, JPQL y SQL Nativo
  Universidad Fidélitas - SC-403 Desarrollo de Aplicaciones Web y Patrones
  
  Este script crea el esquema 'practica', la tabla 'arbol', los usuarios
  y carga datos reales de árboles emblemáticos de Costa Rica con diversas
  durezas de madera (del 1 al 10) para pruebas exhaustivas de consultas.
*/

-- Sección de administración (ejecutar en MySQL Workbench o consola)
DROP DATABASE IF EXISTS practica;
DROP USER IF EXISTS 'usuario_prueba'@'%';
DROP USER IF EXISTS 'usuario_reportes'@'%';

-- Creación del esquema con soporte completo para caracteres en español (tildes, etc.)
CREATE DATABASE practica
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación de usuarios de base de datos (siguiendo el estándar del curso)
CREATE USER 'usuario_prueba'@'%' IDENTIFIED BY 'Usuar1o_Clave.';
CREATE USER 'usuario_reportes'@'%' IDENTIFIED BY 'Usuar1o_Reportes.';

-- Asignación de permisos al usuario de la aplicación
GRANT SELECT, INSERT, UPDATE, DELETE ON practica.* TO 'usuario_prueba'@'%';
GRANT SELECT ON practica.* TO 'usuario_reportes'@'%';
FLUSH PRIVILEGES;

USE practica;

-- --- Sección de Creación de Tablas ---

/*
  Tabla de Árboles
  Atributos:
  1. id_arbol (Clave primaria)
  2. nombre_comun (Nombre popular en Costa Rica)
  3. tipo_flor (Descripción y coloración de la floración)
  4. dureza_madera (Escala de dureza de 1 a 10 - utilizado para consultas de rango equivalente a precio en Producto)
  5. habitat (Región geográfica y tipo de bosque en CR - utilizado para consultas por texto y filtros)
  6. ruta_imagen (Enlace a la fotografía)
  7. activo (Estado del registro)
*/
CREATE TABLE arbol (
  id_arbol INT NOT NULL AUTO_INCREMENT,
  nombre_comun VARCHAR(60) NOT NULL,
  tipo_flor VARCHAR(60) NOT NULL,
  dureza_madera INT NOT NULL CHECK (dureza_madera BETWEEN 1 AND 10),
  habitat VARCHAR(150) NOT NULL,
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_arbol),
  UNIQUE (nombre_comun),
  INDEX ndx_arbol_nombre (nombre_comun),
  INDEX ndx_arbol_dureza (dureza_madera)
) ENGINE = InnoDB;

-- --- Sección de Inserción de Datos Iniciales (Árboles de Costa Rica) ---

INSERT INTO arbol (nombre_comun, tipo_flor, dureza_madera, habitat, ruta_imagen, activo) VALUES
(
  'Balsa',
  'Blanca cremosa grande y solitaria',
  1,
  'Bosques muy húmedos tropicales del Atlántico y Pacífico Sur',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Ochroma_pyramidale_flowers.jpg/1200px-Ochroma_pyramidale_flowers.jpg',
  TRUE
),
(
  'Poró Gigante',
  'Anaranjada brillante en forma de sable o cresta',
  4,
  'Zonas cafetaleras, riberas y montañas del Valle Central',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Erythrina_poeppigiana_01.jpg/1200px-Erythrina_poeppigiana_01.jpg',
  TRUE
),
(
  'Malinche',
  'Roja anaranjada encendida de cuatro pétalos',
  5,
  'Costas del Pacífico, llanuras cálidas y parques urbanos de Costa Rica',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Delonix_regia_tree.jpg/1200px-Delonix_regia_tree.jpg',
  TRUE
),
(
  'Árbol de Guanacaste',
  'Blanca verdosa en inflorescencias esféricas',
  6,
  'Bosque tropical seco, llanuras de Guanacaste y vertiente del Pacífico',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Enterolobium_cyclocarpum_tree.jpg/1200px-Enterolobium_cyclocarpum_tree.jpg',
  TRUE
),
(
  'Roble Sabana',
  'Rosada violácea tubular muy vistosa',
  7,
  'Zonas abiertas y valles intermontanos como el Valle Central',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Tabebuia_rosea_1.jpg/1200px-Tabebuia_rosea_1.jpg',
  TRUE
),
(
  'Cedro Amargo',
  'Blanca pequeña en panículas terminales',
  7,
  'Bosques húmedos y laderas premontanas de ambas vertientes',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Cedrela_odorata_foliage.jpg/1200px-Cedrela_odorata_foliage.jpg',
  TRUE
),
(
  'Cortés Amarillo',
  'Amarilla dorada intensa y acampanada',
  9,
  'Bosques secos y húmedos de la costa pacífica y Valle Central',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Tabebuia_chrysantha.jpg/1200px-Tabebuia_chrysantha.jpg',
  TRUE
),
(
  'Guayacán Real',
  'Azul violácea en pequeños racimos terminales',
  10,
  'Bosque seco tropical de Guanacaste y Península de Nicoya',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Guaiacum_sanctum_flowers.jpg/1200px-Guaiacum_sanctum_flowers.jpg',
  TRUE
),
(
  'Almendro de Montaña',
  'Rosa pálido y lila en panículas erectas',
  10,
  'Llanuras del Caribe Norte y hábitat de la Lapa Verde',
  'https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Dipteryx_panamensis_trunk.jpg/1200px-Dipteryx_panamensis_trunk.jpg',
  TRUE
);
