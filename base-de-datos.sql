-- Base de datos completa de la API de la residencia geriátrica:
-- crea la base, crea las 5 tablas y carga datos de ejemplo.
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

-- Solo el código: si está ocupada no se guarda, se calcula con las reservas
CREATE TABLE habitaciones (
    codigo VARCHAR(255) NOT NULL,
    PRIMARY KEY (codigo)
) ENGINE = InnoDB;

-- La clave es el código de habitación: así una habitación no puede tener dos reservas
CREATE TABLE reservas (
    codigo_habitacion VARCHAR(255) NOT NULL,
    id_residente      INTEGER NOT NULL,
    PRIMARY KEY (codigo_habitacion)
) ENGINE = InnoDB;

-- La clave es el código de habitación: así una habitación no puede tener dos enfermeros
CREATE TABLE asignaciones (
    codigo_habitacion VARCHAR(255) NOT NULL,
    id_enfermero      INTEGER NOT NULL,
    PRIMARY KEY (codigo_habitacion)
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

INSERT INTO habitaciones (codigo) VALUES
    ('HAB-101'),
    ('HAB-102'),
    ('HAB-103'),
    ('HAB-104'),
    ('HAB-105');

-- Rosa en la 101, Alberto en la 102, Marta en la 103 (Héctor queda sin habitación)
INSERT INTO reservas (codigo_habitacion, id_residente) VALUES
    ('HAB-101', 1),
    ('HAB-102', 2),
    ('HAB-103', 3);

-- Lucía atiende la 101 y la 102, Martín la 103 (Valeria queda libre)
INSERT INTO asignaciones (codigo_habitacion, id_enfermero) VALUES
    ('HAB-101', 1),
    ('HAB-102', 1),
    ('HAB-103', 2);

-- 4. Para comprobar que se cargó todo
SELECT * FROM residentes;
SELECT * FROM enfermeros;
SELECT * FROM habitaciones;
SELECT * FROM reservas;
SELECT * FROM asignaciones;
