ALTER TABLE itevent ALTER COLUMN eventtype TYPE character varying(8);
update itevent set eventtype='CALL';
ALTER TABLE itevent RENAME starttime  TO eventtime;
