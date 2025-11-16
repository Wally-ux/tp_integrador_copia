-- ============================================================
-- CREACIÓN DE BASE DE DATOS
-- ============================================================
CREATE DATABASE IF NOT EXISTS clinica CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE clinica;

-- ============================================================
-- TABLA: historia_clinica (Entidad B)
-- ============================================================
CREATE TABLE historia_clinica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,               -- Baja lógica
    nro_historia VARCHAR(20) NOT NULL UNIQUE,      -- UNIQUE, máx. 20
    grupo_sanguineo VARCHAR(3),                    -- Se manejará como Enum en Java
    antecedentes TEXT,
    medicacion_actual TEXT,
    observaciones TEXT
) ENGINE=InnoDB;

-- ============================================================
-- TABLA: paciente (Entidad A)
-- ============================================================
CREATE TABLE paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,               -- Baja lógica
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,               -- NOT NULL, UNIQUE, máx. 15
    fecha_nacimiento DATE,
    historia_clinica_id BIGINT UNIQUE,             -- Relación 1→1 con HistoriaClinica
    CONSTRAINT fk_paciente_historia FOREIGN KEY (historia_clinica_id)
        REFERENCES historia_clinica(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- ============================================================
-- ÍNDICES ADICIONALES (opcional para búsquedas)
-- ============================================================
CREATE INDEX idx_paciente_apellido ON paciente(apellido);
CREATE INDEX idx_historia_nro ON historia_clinica(nro_historia);
