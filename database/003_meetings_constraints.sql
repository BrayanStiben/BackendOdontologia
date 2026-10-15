-- Run once after 002 and the reviewed backfill of legacy meetings.
-- Fail and roll back if legacy data has missing values, overlaps or invalid ranges.
BEGIN;

ALTER TABLE meeting ALTER COLUMN patientid SET NOT NULL;
ALTER TABLE meeting ALTER COLUMN doctorid SET NOT NULL;
ALTER TABLE meeting ALTER COLUMN date SET NOT NULL;
ALTER TABLE meeting ALTER COLUMN status SET NOT NULL;
ALTER TABLE meeting ALTER COLUMN ends_at SET NOT NULL;
ALTER TABLE meeting ALTER COLUMN chair_id SET NOT NULL;
ALTER TABLE meeting ALTER COLUMN reason SET NOT NULL;
ALTER TABLE meeting ADD CONSTRAINT fk_meeting_chair FOREIGN KEY (chair_id) REFERENCES dental_chair (id);
ALTER TABLE meeting ADD CONSTRAINT ck_meeting_time CHECK (ends_at > date);
ALTER TABLE meeting ADD CONSTRAINT ck_meeting_reason CHECK (length(trim(reason)) BETWEEN 1 AND 500);
ALTER TABLE meeting ADD CONSTRAINT ck_meeting_status
    CHECK (status IN ('Pendiente', 'Confirmada', 'Cancelada', 'Completada'));

-- Remove ONLY single-column UNIQUE constraints introduced by the previous OneToOne mappings.
-- Primary keys, foreign keys and unrelated uniqueness constraints are preserved.
DO $$
DECLARE constraint_row RECORD;
BEGIN
    FOR constraint_row IN
        SELECT c.conname
        FROM pg_constraint c
        JOIN pg_attribute a ON a.attrelid = c.conrelid AND a.attnum = c.conkey[1]
        WHERE c.conrelid = 'meeting'::regclass AND c.contype = 'u'
          AND cardinality(c.conkey) = 1 AND a.attname IN ('patientid', 'doctorid')
    LOOP
        EXECUTE format('ALTER TABLE meeting DROP CONSTRAINT %I', constraint_row.conname);
    END LOOP;
END $$;

-- Half-open ranges [start, end): back-to-back appointments are allowed.
-- Constraints also protect against overlapping inserts/updates from concurrent transactions.
ALTER TABLE meeting ADD CONSTRAINT ex_meeting_doctor_time
    EXCLUDE USING gist (doctorid WITH =, tstzrange(date, ends_at, '[)') WITH &&)
    WHERE (status IN ('Pendiente', 'Confirmada'));
ALTER TABLE meeting ADD CONSTRAINT ex_meeting_patient_time
    EXCLUDE USING gist (patientid WITH =, tstzrange(date, ends_at, '[)') WITH &&)
    WHERE (status IN ('Pendiente', 'Confirmada'));
ALTER TABLE meeting ADD CONSTRAINT ex_meeting_chair_time
    EXCLUDE USING gist (chair_id WITH =, tstzrange(date, ends_at, '[)') WITH &&)
    WHERE (status IN ('Pendiente', 'Confirmada'));

COMMIT;
