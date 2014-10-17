CREATE TABLE smppstatus
(
  id numeric NOT NULL,
  commandstatus integer,
  senderror character varying(255),
  smppfinalstatus character varying(255),
  smppid numeric,
  notification_id numeric,
  CONSTRAINT smppstatus_pkey PRIMARY KEY (id)
);


 ALTER TABLE smsnotification DROP COLUMN smppid;
 ALTER TABLE smsnotification DROP COLUMN commandstatus;
 ALTER TABLE smsnotification DROP COLUMN smppfinalstatus;