CREATE TABLE itevent
(
  id numeric NOT NULL,
  completed boolean NOT NULL,
  euro double precision NOT NULL,
  eventtype integer,
  remind boolean NOT NULL,
  rub double precision NOT NULL,
  starttime timestamp without time zone,
  text character varying(255),
  CONSTRAINT itevent_pkey PRIMARY KEY (id)
);
CREATE TABLE itevent_client
(
  abstract_client_id numeric,
  it_event_id numeric NOT NULL,
  CONSTRAINT itevent_client_pkey PRIMARY KEY (it_event_id)
);
CREATE TABLE itevent_employee
(
  employee_id numeric,
  it_event_id numeric NOT NULL,
  CONSTRAINT itevent_employee_pkey PRIMARY KEY (it_event_id)
);
