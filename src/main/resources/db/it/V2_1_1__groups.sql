CREATE TABLE itgroup
(
  id numeric NOT NULL,
  name character varying(255),
  owner_id numeric,
  company_id numeric,
  CONSTRAINT itgroup_pkey PRIMARY KEY (id)
);
CREATE TABLE jurid_person_group
(
  juridicalperson_id numeric NOT NULL,
  group_id numeric NOT NULL
);