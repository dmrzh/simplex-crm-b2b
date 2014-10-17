CREATE TABLE itnote
(
  id numeric NOT NULL,
  date timestamp without time zone NOT NULL,
  text character varying(1024),
  author_id numeric,
  juridicalperson_id numeric,
  CONSTRAINT itnote_pkey PRIMARY KEY (id)
);

CREATE INDEX "date_it_note_index"
  ON itnote
  USING btree
  (date);

