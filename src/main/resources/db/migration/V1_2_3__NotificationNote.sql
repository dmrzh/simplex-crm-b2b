alter table smsnotification ADD COLUMN note  varchar(255);
CREATE INDEX notificationstatusidx ON smsnotification USING btree (notificationstatus);



