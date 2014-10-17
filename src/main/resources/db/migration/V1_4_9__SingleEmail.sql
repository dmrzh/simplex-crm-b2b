CREATE TABLE singleemail
(
  id numeric NOT NULL,
  notificationstatus character varying(255) NOT NULL,
  startdate timestamp without time zone,
  subject character varying(255),
  text character varying(2048),
  from_id numeric,
  to_id numeric,
  CONSTRAINT singleemail_pkey PRIMARY KEY (id)
) ;
CREATE INDEX emailnotificationstatusidx  ON singleemail  (notificationstatus);