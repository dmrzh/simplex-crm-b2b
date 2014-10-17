

CREATE TABLE smsnotification
(
  id numeric NOT NULL,
  notificationstatus character varying(255) NOT NULL,
  senderror character varying(255),
  client_id numeric,
  meeting_id numeric,
  CONSTRAINT smsnotification_pkey PRIMARY KEY (id)
);

insert into smsnotification(id, meeting_id, client_id , notificationstatus ,  senderror ) SELECT id, id, client_id, notificationstatus, senderror FROM meeting;

alter table  meeting DROP COLUMN senderror;
alter table  meeting DROP COLUMN notificationstatus;




