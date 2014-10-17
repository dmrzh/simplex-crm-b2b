TRUNCATE TABLE abstractclient;
ALTER TABLE abstractclient ADD COLUMN deleted character(1);
ALTER TABLE abstractclient ALTER COLUMN deleted SET NOT NULL;


INSERT INTO abstractclient(id, deleted) SELECT id, deleted FROM client;
INSERT INTO abstractclient(id, deleted) SELECT id, 'N' FROM juridicalperson;

CREATE INDEX abstractclient_deleted_idx
  ON abstractclient
  USING btree
  (deleted);

ALTER TABLE client DROP COLUMN deleted;
ALTER TABLE juridicalperson DROP COLUMN deleted;

