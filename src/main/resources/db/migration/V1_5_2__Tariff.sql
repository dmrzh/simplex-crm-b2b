CREATE TABLE tariff
(
  id numeric NOT NULL,
  aclass character varying(255),
  constantcost integer NOT NULL,
  CONSTRAINT tariff_pkey PRIMARY KEY (id)
) ;
INSERT INTO tariff(id, constantcost)VALUES (1, 10);

ALTER TABLE company ADD COLUMN currenttariff_id numeric;
UPDATE company SET currenttariff_id =1;
ALTER TABLE company ALTER COLUMN currenttariff_id SET NOT NULL;
