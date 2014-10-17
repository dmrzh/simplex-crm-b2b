ALTER TABLE client ADD COLUMN deleted character(1);
UPDATE client SET deleted='N';
ALTER TABLE client ALTER COLUMN deleted SET NOT NULL;
CREATE INDEX client_deleted_idx  ON client (deleted);