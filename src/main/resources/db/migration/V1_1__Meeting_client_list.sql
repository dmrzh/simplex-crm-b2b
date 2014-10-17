--
alter table dynamiccolumn ALTER COLUMN showInList type character(1);

CREATE TABLE meeting_client(
  meeting_id numeric NOT NULL,
  clientlist_id numeric NOT NULL,
  CONSTRAINT meeting_client_pkey PRIMARY KEY (meeting_id, clientlist_id)
)
