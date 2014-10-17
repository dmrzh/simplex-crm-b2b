
ALTER TABLE smsnotification ADD COLUMN commandstatus integer;
ALTER TABLE masssms ADD COLUMN active character(1);
update masssms set active='Y';
ALTER TABLE masssms ALTER COLUMN active SET NOT NULL;