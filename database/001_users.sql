-- Manual PostgreSQL migration: back up and verify the existing schema before applying.
-- Table "User" is quoted; existing column names follow PostgreSQL's unquoted lowercase convention.
BEGIN;

CREATE TABLE IF NOT EXISTS "User" (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    rol VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

-- Preserve existing users; numeric phone values become text.
ALTER TABLE "User" ALTER COLUMN phone TYPE VARCHAR(20) USING phone::text;
ALTER TABLE "User" ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;

-- Reject concurrent duplicate emails, including legacy case and surrounding spaces.
-- Existing duplicates must be resolved explicitly before this statement can succeed.
CREATE UNIQUE INDEX IF NOT EXISTS uq_user_email_normalized ON "User" (lower(trim(email)));

CREATE TABLE IF NOT EXISTS user_audit (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "User" (id),
    actor VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL CHECK (action IN ('CREATE', 'UPDATE')),
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    previous_role VARCHAR(255),
    new_role VARCHAR(255) NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_user_audit_user_time ON user_audit (user_id, occurred_at);

COMMIT;
