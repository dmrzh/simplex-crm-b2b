CREATE TABLE smtpsettings
(
  id numeric NOT NULL,
  mailsmtpauth boolean NOT NULL,
  mailsmtphost character varying(255),
  mailsmtpport integer NOT NULL,
  mailsmtpsocketfactoryclass character varying(255),
  mailsmtpsocketfactoryfallback boolean,
  mailtransportprotocol character varying(255),
  smtppassword character varying(255),
  smtpusername character varying(255),
  company_id numeric,
  CONSTRAINT smtpsettings_pkey PRIMARY KEY (id)
);
ALTER TABLE category ALTER COLUMN name SET  NOT NULL;