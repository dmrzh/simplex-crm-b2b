--Paments

ALTER TABLE  company ADD COLUMN balance INTEGER;
UPDATE company SET balance=0;
ALTER TABLE  company ALTER COLUMN balance SET NOT NULL;

CREATE TABLE payment
(
  id numeric NOT NULL,
  amount integer NOT NULL,
  payed boolean NOT NULL,
  startdate timestamp without time zone NOT NULL,
  uuid character varying(255) NOT NULL,
  company_id numeric NOT NULL,
  CONSTRAINT payment_pkey PRIMARY KEY (id)
) ;