
CREATE TABLE abstractclient
(
  id numeric NOT NULL,
  CONSTRAINT abstractclient_pkey PRIMARY KEY (id)
) ;

CREATE TABLE juridicalperson
(
  address character varying(255),
  city character varying(255),
  deleted boolean NOT NULL,
  name character varying(255),
  note character varying(255),
  region character varying(255),
  site character varying(255),
  id numeric NOT NULL,
  company_id numeric NOT NULL,
  CONSTRAINT juridicalperson_pkey PRIMARY KEY (id)
);


CREATE INDEX juridicalperson_company_idx
  ON juridicalperson
  USING btree
  (company_id);

ALTER TABLE client ADD COLUMN juridicalperson_id numeric;
INSERT INTO abstractclient(SELECT id FROM client WHERE id NOT IN (SELECT id FROM abstractclient));


