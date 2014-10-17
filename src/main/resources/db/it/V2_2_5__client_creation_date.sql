ALTER TABLE abstractclient ADD COLUMN creationDate timestamp without time zone;
update abstractclient set creationDate= now();