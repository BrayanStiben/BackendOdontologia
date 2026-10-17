-- Migration 004: All full features corresponding to Mockups and Class Diagram
-- Execute after 001, 002, 003.
BEGIN;

-- 1. User enhancements for authentication and dental profile
ALTER TABLE "User" ADD COLUMN IF NOT EXISTS password VARCHAR(255);
ALTER TABLE "User" ADD COLUMN IF NOT EXISTS tp VARCHAR(50);
ALTER TABLE "User" ADD COLUMN IF NOT EXISTS clinic_name VARCHAR(150);
ALTER TABLE "User" ADD COLUMN IF NOT EXISTS specialty VARCHAR(100);
ALTER TABLE "User" ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;

-- 2. Inventory: Medicine
CREATE TABLE IF NOT EXISTS medicine (
    id UUID PRIMARY KEY,
    medicine_name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    stock INTEGER NOT NULL DEFAULT 0,
    minimum_stock INTEGER NOT NULL DEFAULT 5,
    unit_cost DOUBLE PRECISION NOT NULL DEFAULT 0.0
);

-- 3. Treatment and Treatment Details
CREATE TABLE IF NOT EXISTS treatment (
    id UUID PRIMARY KEY,
    code VARCHAR(50),
    description VARCHAR(500) NOT NULL,
    cost DOUBLE PRECISION NOT NULL DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS t_detail (
    id UUID PRIMARY KEY,
    treatment_id UUID NOT NULL REFERENCES treatment (id) ON DELETE CASCADE,
    medicine_id UUID NOT NULL REFERENCES medicine (id),
    units INTEGER NOT NULL DEFAULT 1
);

-- 4. Clinical History and Evolutions
CREATE TABLE IF NOT EXISTS clinical_history (
    id UUID PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES "User" (id),
    doctor_id BIGINT REFERENCES "User" (id),
    treatment_id UUID REFERENCES treatment (id),
    record_date TIMESTAMP WITH TIME ZONE NOT NULL,
    tooth_piece VARCHAR(50),
    procedure_name VARCHAR(200),
    diagnosis VARCHAR(1000),
    observations VARCHAR(2000),
    signed BOOLEAN NOT NULL DEFAULT TRUE,
    motivo_consulta VARCHAR(500),
    enfermedad_actual VARCHAR(1000),
    blood_pressure VARCHAR(20),
    heart_rate VARCHAR(20),
    temperature VARCHAR(20),
    weight VARCHAR(20),
    atm_articulation VARCHAR(100),
    soft_tissues VARCHAR(100),
    oral_hygiene VARCHAR(50),
    plaque_index VARCHAR(20),
    dental_calculus BOOLEAN DEFAULT FALSE,
    food_impaction BOOLEAN DEFAULT FALSE,
    primary_diagnosis VARCHAR(250),
    secondary_diagnosis VARCHAR(250),
    prognosis VARCHAR(100),
    suggested_plan VARCHAR(2000)
);
CREATE INDEX IF NOT EXISTS idx_clinical_history_patient ON clinical_history (patient_id, record_date DESC);

-- 5. Electronic Invoicing (DIAN)
CREATE TABLE IF NOT EXISTS bill (
    id UUID PRIMARY KEY,
    bill_number VARCHAR(50) NOT NULL UNIQUE,
    patient_id BIGINT NOT NULL REFERENCES "User" (id),
    receptionist_id BIGINT REFERENCES "User" (id),
    issue_date TIMESTAMP WITH TIME ZONE NOT NULL,
    payment_method VARCHAR(50),
    subtotal DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    discount DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    tax DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    total_cost DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    observations VARCHAR(1000),
    cufe VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_bill_patient ON bill (patient_id, issue_date DESC);

CREATE TABLE IF NOT EXISTS bill_detail (
    id UUID PRIMARY KEY,
    bill_id UUID NOT NULL REFERENCES bill (id) ON DELETE CASCADE,
    meeting_id UUID REFERENCES meeting (id),
    treatment_id UUID REFERENCES treatment (id),
    code VARCHAR(50),
    description VARCHAR(500) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    discount_percent DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    subtotal DOUBLE PRECISION NOT NULL DEFAULT 0.0
);

-- 6. Notifications
CREATE TABLE IF NOT EXISTS notification (
    id UUID PRIMARY KEY,
    meeting_id UUID REFERENCES meeting (id),
    type VARCHAR(50) NOT NULL,
    recipient VARCHAR(254) NOT NULL,
    message VARCHAR(2000),
    send_date TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Seed initial basic data if tables are empty
INSERT INTO dental_chair (id, name, active)
VALUES (1, 'Consultorio 20 - Sillón Principal', TRUE),
       (2, 'Consultorio 21 - Sillón Secundario', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO treatment (id, code, description, cost)
VALUES ('00000000-0000-0000-0000-000000000001', 'CUPS-890201', 'Consulta de primera vez por odontologia general', 60000),
       ('00000000-0000-0000-0000-000000000002', 'CUPS-232101', 'Resina de fotocurado de alta densidad', 140000),
       ('00000000-0000-0000-0000-000000000003', 'CUPS-997301', 'Detartraje y profilaxis dental completa', 80000)
ON CONFLICT (id) DO NOTHING;

INSERT INTO medicine (id, medicine_name, description, stock, minimum_stock, unit_cost)
VALUES ('00000000-0000-0000-0000-000000000011', 'Resina Z350 (4g)', 'Resina compuesta restauradora', 2, 5, 45000),
       ('00000000-0000-0000-0000-000000000012', 'Anestesia Lidocaina 2%', 'Tubo de anestesia dental', 25, 10, 3200),
       ('00000000-0000-0000-0000-000000000013', 'Amoxicilina 500mg', 'Antibiotico capsulas', 15, 8, 1200)
ON CONFLICT (id) DO NOTHING;

COMMIT;

