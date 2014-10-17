ALTER TABLE juridicalperson ADD COLUMN name character varying(255);
update juridicalperson set name=(select array_to_string(array_agg(names), '; ') from juridicalperson_names where juridicalperson_id=id);

 DROP TABLE juridicalperson_names;