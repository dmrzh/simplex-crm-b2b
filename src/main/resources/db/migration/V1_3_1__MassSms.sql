CREATE TABLE masssms
(
  id numeric NOT NULL,
  name character varying(255),
  startdate timestamp without time zone,
  text character varying(255),
  company_id numeric NOT NULL,
  CONSTRAINT masssms_pkey PRIMARY KEY (id)
);
CREATE TABLE masssms_smsnotification
(
  masssms_id numeric NOT NULL,
  smsnotificationlist_id numeric NOT NULL,
  CONSTRAINT masssms_smsnotification_smsnotificationlist_id_key UNIQUE (smsnotificationlist_id)
)  ;
