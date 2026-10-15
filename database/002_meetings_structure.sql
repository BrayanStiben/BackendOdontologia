-- Run once after 001_users.sql. Review the real schema before applying.
-- Assumes the original unquoted Meeting table resolves to public.meeting.
BEGIN;

CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA public;

CREATE TABLE IF NOT EXISTS dental_chair (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS meeting (
    id UUID PRIMARY KEY,
    patientid BIGINT NOT NULL REFERENCES "User" (id),
    doctorid BIGINT NOT NULL REFERENCES "User" (id),
    date TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(255) NOT NULL
);

ALTER TABLE meeting ADD COLUMN IF NOT EXISTS ends_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE meeting ADD COLUMN IF NOT EXISTS chair_id BIGINT;
ALTER TABLE meeting ADD COLUMN IF NOT EXISTS reason VARCHAR(500);
ALTER TABLE meeting ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;

-- Original java.util.Date mappings can have timestamp-without-time-zone columns.
-- This conversion assumes legacy timestamps represent Colombian local time.
-- Change the zone BEFORE running if the old application stored UTC or another zone.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = current_schema() AND table_name = 'meeting'
          AND column_name = 'date' AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE meeting ALTER COLUMN date TYPE TIMESTAMP WITH TIME ZONE
            USING date AT TIME ZONE 'America/Bogota';
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS meeting_audit (
    id UUID PRIMARY KEY,
    meeting_id UUID NOT NULL REFERENCES meeting (id),
    actor VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL CHECK (action IN ('CREATE', 'UPDATE')),
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_meeting_audit_time ON meeting_audit (meeting_id, occurred_at);

COMMIT;

-- BEFORE 003: register real dental chairs and fill ends_at, chair_id and reason
-- for EVERY legacy meeting, using verified data. No durations or chairs are invented here.
