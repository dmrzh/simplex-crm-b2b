ALTER TABLE masssms ADD COLUMN deleted character(1);
UPDATE masssms SET deleted='N';
ALTER TABLE masssms ALTER COLUMN deleted SET NOT NULL;
CREATE INDEX deletedidx  ON masssms (deleted);