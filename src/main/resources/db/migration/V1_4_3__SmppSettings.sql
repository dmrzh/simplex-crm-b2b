CREATE TABLE smppsettings
(
  id numeric NOT NULL,
  destaddrnpi character varying(255),
  destaddrton character varying(255),
  host character varying(255) NOT NULL,
  login character varying(255) NOT NULL,
  messageclass character varying(255),
  password character varying(255) NOT NULL,
  port integer NOT NULL,
  priorityflag integer NOT NULL,
  servicetype character varying(255),
  sourceaddrnpi character varying(255),
  sourceaddrton character varying(255),
  CONSTRAINT smppsettings_pkey PRIMARY KEY (id)
) ;


INSERT INTO smppsettings (id, destaddrnpi, destaddrton, host, login, messageclass, password, port, priorityflag, servicetype, sourceaddrnpi, sourceaddrton) VALUES (10, 'ISDN', 'INTERNATIONAL', '78.46.32.24', '471', NULL, 'gV10J7am', 2775, 0, NULL, 'UNKNOWN', 'UNKNOWN');
INSERT INTO smppsettings (id, destaddrnpi, destaddrton, host, login, messageclass, password, port, priorityflag, servicetype, sourceaddrnpi, sourceaddrton) VALUES (20, 'ISDN', 'INTERNATIONAL', 'smpp3.websms.ru', 'dmrzh2', 'CLASS1', 'uw0cho0E', 2222, 1, 'CMT', 'UNKNOWN', 'ALPHANUMERIC');
