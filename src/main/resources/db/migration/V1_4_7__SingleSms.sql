CREATE TABLE SingleSms
(
  id numeric NOT NULL,
  startdate timestamp without time zone,
  text character varying(255),
  smsnotification_id numeric,
  CONSTRAINT directsms_pkey PRIMARY KEY (id)
)