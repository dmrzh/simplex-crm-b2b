CREATE TABLE juridicalperson_names
(
  juridicalperson_id numeric NOT NULL,
  names character varying(255)
);
insert into juridicalperson_names select id, name from juridicalperson;

ALTER TABLE juridicalperson DROP COLUMN name;