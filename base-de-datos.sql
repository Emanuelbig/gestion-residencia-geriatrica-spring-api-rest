-- Base de datos completa de la API de la residencia geriátrica:
-- crea la base, crea las 3 tablas y carga datos de ejemplo.
--
-- Cómo usarlo: abrirlo en MySQL Workbench (File > Open SQL Script) y ejecutarlo con el rayo (Ctrl+Shift+Enter).
-- Se puede ejecutar las veces que quieras: siempre deja la base como recién instalada.
-- ATENCIÓN: si ya tenías datos en la base geriatrico, se borran.
--
-- No es obligatorio: si no lo ejecutás, al arrancar la aplicación Hibernate crea las tablas solo
-- (vacías) a partir de las clases @Entity. Este script sirve para tener datos de prueba de una.

-- 1. La base
CREATE DATABASE IF NOT EXISTS geriatrico CHARACTER SET utf8mb4;
USE geriatrico;

-- 2. Las tablas (las mismas que genera Hibernate a partir de las entidades)
-- Primero se borran las que apuntan a otras (habitaciones tiene FK a residentes y enfermeros).
-- reservas y asignaciones son las tablas de la versión anterior, de 5 tablas.
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS asignaciones;
DROP TABLE IF EXISTS habitaciones;
DROP TABLE IF EXISTS residentes;
DROP TABLE IF EXISTS enfermeros;

-- Residente y Enfermero heredan de Persona: cada uno tiene su tabla con id, dni, nombre y apellido
CREATE TABLE residentes (
    id       INTEGER NOT NULL AUTO_INCREMENT,
    dni      VARCHAR(255),
    nombre   VARCHAR(255),
    apellido VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE enfermeros (
    id       INTEGER NOT NULL AUTO_INCREMENT,
    dni      VARCHAR(255),
    nombre   VARCHAR(255),
    apellido VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

-- La habitación guarda quién la ocupa y quién la atiende. NULL = no hay.
--   id_residente: FK + UNIQUE → un residente ocupa una sola habitación (uno a uno)
--   id_enfermero: FK sin UNIQUE → un enfermero atiende varias habitaciones (uno a muchos)
-- Las FK no dejan guardar un residente o enfermero que no existe, ni borrar uno que está en una habitación.
CREATE TABLE habitaciones (
    codigo       VARCHAR(255) NOT NULL,
    id_residente INTEGER NULL,
    id_enfermero INTEGER NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT uk_habitacion_residente UNIQUE (id_residente),
    CONSTRAINT fk_habitacion_residente FOREIGN KEY (id_residente) REFERENCES residentes (id),
    CONSTRAINT fk_habitacion_enfermero FOREIGN KEY (id_enfermero) REFERENCES enfermeros (id)
) ENGINE = InnoDB;

-- 3. Datos de ejemplo (los mismos que trae la versión de consola)

-- Residentes: ids 1 a 4
INSERT INTO residentes (id, dni, nombre, apellido) VALUES
    (1, '20345678', 'Rosa', 'Fernández'),
    (2, '18765432', 'Alberto', 'Gómez'),
    (3, '21987654', 'Marta', 'Sánchez'),
    (4, '19456123', 'Héctor', 'Ramírez');

-- Enfermeros: ids 1 a 3
INSERT INTO enfermeros (id, dni, nombre, apellido) VALUES
    (1, '33456789', 'Lucía', 'Pérez'),
    (2, '35678901', 'Martín', 'López'),
    (3, '37890123', 'Valeria', 'Torres');

-- Rosa en la 101, Alberto en la 102, Marta en la 103 (Héctor queda sin habitación).
-- Lucía atiende la 101 y la 102, Martín la 103 (Valeria queda libre). La 104 y la 105 están vacías.
INSERT INTO habitaciones (codigo, id_residente, id_enfermero) VALUES
    ('HAB-101', 1, 1),
    ('HAB-102', 2, 1),
    ('HAB-103', 3, 2),
    ('HAB-104', NULL, NULL),
    ('HAB-105', NULL, NULL);

-- 4. Para comprobar que se cargó todo
SELECT * FROM residentes;
SELECT * FROM enfermeros;
SELECT * FROM habitaciones;
