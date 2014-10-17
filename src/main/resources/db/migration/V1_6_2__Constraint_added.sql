DELETE from smsnotification where client_id is null;
ALTER TABLE smsnotification  ALTER COLUMN client_id  SET NOT NULL;