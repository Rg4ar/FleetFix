-- ============================================================
-- FleetFix — Script de base de datos SQLite
-- Versión: 1.0 | Abril 2025
-- Descripción: Crea todas las tablas e inserta datos iniciales
-- ============================================================

-- Tabla: Usuarios
-- Almacena los usuarios del sistema con sus credenciales
CREATE TABLE IF NOT EXISTS Usuarios (
    id_usuario     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_usuario TEXT    NOT NULL UNIQUE,
    contrasena     TEXT    NOT NULL,
    activo         INTEGER NOT NULL DEFAULT 1
);

-- Tabla: Niveles
-- Catálogo de niveles de prioridad para los tickets
CREATE TABLE IF NOT EXISTS Niveles (
    id_nivel INTEGER PRIMARY KEY,
    nombre   TEXT NOT NULL
);

-- Tabla: Vehiculos
-- Registro de la flota vehicular de la empresa
CREATE TABLE IF NOT EXISTS Vehiculos (
    id_vehiculo INTEGER PRIMARY KEY AUTOINCREMENT,
    placa       TEXT    NOT NULL,
    modelo      TEXT    NOT NULL,
    estado      TEXT    NOT NULL DEFAULT 'Operativo'
);

-- Tabla: Tickets
-- Registro de fallas y mantenimientos reportados
-- Nota: el campo 'vehiculo' guarda el tipo de vehiculo como texto (SUV, Pickup, etc.)
--       en lugar de una FK a la tabla Vehiculos, para mayor flexibilidad
CREATE TABLE IF NOT EXISTS Tickets (
    id_ticket     INTEGER PRIMARY KEY AUTOINCREMENT,
    vehiculo      TEXT    NOT NULL,
    id_nivel      INTEGER NOT NULL,
    titulo        TEXT    NOT NULL,
    descripcion   TEXT    NOT NULL,
    estado        TEXT    NOT NULL DEFAULT 'Abierto',
    falla_critica INTEGER NOT NULL DEFAULT 0,
    creado_en     TEXT    NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (id_nivel) REFERENCES Niveles(id_nivel)
);

-- ============================================================
-- DATOS INICIALES
-- ============================================================

-- Usuarios del sistema
INSERT INTO Usuarios (nombre_usuario, contrasena, activo) VALUES ('admin',  'admin123', 1);
INSERT INTO Usuarios (nombre_usuario, contrasena, activo) VALUES ('raini',  'fleetfix', 1);

-- Niveles de prioridad
INSERT INTO Niveles VALUES (1, 'Bajo');
INSERT INTO Niveles VALUES (2, 'Medio');
INSERT INTO Niveles VALUES (3, 'Alto');
INSERT INTO Niveles VALUES (4, 'Critico');

-- Vehiculos de la flota
INSERT INTO Vehiculos (placa, modelo, estado) VALUES ('ABC-001', 'Toyota Hilux',        'Operativo');
INSERT INTO Vehiculos (placa, modelo, estado) VALUES ('XYZ-002', 'Ford F-150',          'Revision');
INSERT INTO Vehiculos (placa, modelo, estado) VALUES ('DEF-003', 'Nissan Frontier',     'Operativo');
INSERT INTO Vehiculos (placa, modelo, estado) VALUES ('GHI-004', 'Chevrolet Silverado', 'Operativo');

-- ============================================================
-- NOTAS
-- ============================================================
-- Estado de tickets posibles: 'Abierto', 'En progreso', 'Resuelto', 'Cerrado'
-- falla_critica: 1 cuando id_nivel = 4 (Critico), 0 en cualquier otro caso
-- creado_en: se genera automaticamente con datetime('now') al insertar
-- ============================================================
