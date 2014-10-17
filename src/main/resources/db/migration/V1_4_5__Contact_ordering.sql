ALTER TABLE contact ADD COLUMN index integer;
ALTER TABLE contact ALTER COLUMN client_id DROP NOT NULL;