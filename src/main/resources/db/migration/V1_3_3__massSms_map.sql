DROP TABLE masssms_smsnotification;
ALTER TABLE smsnotification ADD COLUMN masssms_id numeric;

ALTER TABLE smsnotification ALTER COLUMN client_id SET NOT NULL;