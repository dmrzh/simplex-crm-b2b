
--
-- Name: category; Type: TABLE; Schema: public;
--

CREATE TABLE category (
    id numeric NOT NULL,
    name character varying(255),
    company_id numeric NOT NULL
);


--
-- Name: client; Type: TABLE; Schema: public;
--

CREATE TABLE client (
    id numeric NOT NULL,
    address character varying(255),
    bithday date,
    firstname character varying(255),
    lastname character varying(255),
    middlename character varying(255),
    note character varying(255),
    source character varying(255),
    category_id numeric,
    company_id numeric NOT NULL
);


--
-- Name: clientdynamicvalue; Type: TABLE; Schema: public;
--

CREATE TABLE clientdynamicvalue (
    id numeric NOT NULL,
    booleanvalue boolean,
    datevalue timestamp without time zone,
    value character varying(255),
    clientvalue_id numeric,
    dynamiccolumn_id numeric,
    client_id numeric
);


--
-- Name: company; Type: TABLE; Schema: public;
--

CREATE TABLE company (
    id numeric NOT NULL,
    companyphone character varying(15) NOT NULL,
    defaultremindertime integer,
    defaultsmstext character varying(255),
    finishworkinghours integer,
    name character varying(255) NOT NULL,
    registreddate timestamp without time zone NOT NULL,
    smsfrom character varying(255),
    startworkinghours integer,
    timezone character varying(255),
    referer_id numeric,
    CONSTRAINT company_defaultremindertime_check CHECK (((defaultremindertime >= 0) AND (defaultremindertime <= 720))),
    CONSTRAINT company_finishworkinghours_check CHECK (((finishworkinghours >= 0) AND (finishworkinghours <= 24))),
    CONSTRAINT company_startworkinghours_check CHECK (((startworkinghours >= 0) AND (startworkinghours <= 24)))
);


--
-- Name: contact; Type: TABLE; Schema: public;
--

CREATE TABLE contact (
    id numeric NOT NULL,
    contacttype character varying(255),
    note character varying(255),
    value character varying(255),
    client_id numeric NOT NULL
);


--
-- Name: dynamiccolumn; Type: TABLE; Schema: public;
--

CREATE TABLE dynamiccolumn (
    id numeric NOT NULL,
    description character varying(255),
    exstendedtables character varying(255),
    name character varying(255),
    showinlist character(1) NOT NULL,
    type character varying(255),
    company_id numeric
);


--
-- Name: employee; Type: TABLE; Schema: public;
--

CREATE TABLE employee (
    id numeric NOT NULL,
    activationcode character varying(255),
    disable boolean NOT NULL,
    email character varying(255) NOT NULL,
    fio character varying(255),
    password character varying(32) NOT NULL,
    role character varying(255),
    showonlymymeetings boolean NOT NULL,
    company_id numeric NOT NULL
);


--
-- Name: hibernate_unique_key; Type: TABLE; Schema: public;
--

CREATE TABLE hibernate_unique_key (
    next_hi integer
);


--
-- Name: ht_category; Type: TABLE; Schema: public;
--

CREATE TABLE ht_category (
    id numeric NOT NULL,
    hib_sess_id character(36)
);

--
-- Name: ht_client; Type: TABLE; Schema: public;
--

CREATE TABLE ht_client (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: ht_clientdynamicvalue; Type: TABLE; Schema: public;
--

CREATE TABLE ht_clientdynamicvalue (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: ht_company; Type: TABLE; Schema: public;
--

CREATE TABLE ht_company (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: ht_contact; Type: TABLE; Schema: public;
--

CREATE TABLE ht_contact (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: ht_dynamiccolumn; Type: TABLE; Schema: public;
--

CREATE TABLE ht_dynamiccolumn (
    id numeric NOT NULL,
    hib_sess_id character(36)
);



--
-- Name: ht_employee; Type: TABLE; Schema: public;
--

CREATE TABLE ht_employee (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: ht_meeting; Type: TABLE; Schema: public;
--

CREATE TABLE ht_meeting (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: ht_meetingdynamicvalue; Type: TABLE; Schema: public;
--

CREATE TABLE ht_meetingdynamicvalue (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: ht_passwordreset; Type: TABLE; Schema: public;
--

CREATE TABLE ht_passwordreset (
    id numeric NOT NULL,
    hib_sess_id character(36)
);


--
-- Name: meeting; Type: TABLE; Schema: public;
--

CREATE TABLE meeting (
    id numeric NOT NULL,
    begindate timestamp without time zone NOT NULL,
    enddate timestamp without time zone NOT NULL,
    note character varying(255),
    notificationdate timestamp without time zone,
    notificationstatus character varying(255) NOT NULL,
    rememberbeforehours integer,
    senderror character varying(255),
    smstext character varying(255) NOT NULL,
    client_id numeric,
    employee_id numeric
);


--
-- Name: meetingdynamicvalue; Type: TABLE; Schema: public;
--

CREATE TABLE meetingdynamicvalue (
    id numeric NOT NULL,
    booleanvalue boolean,
    datevalue timestamp without time zone,
    value character varying(255),
    clientvalue_id numeric,
    dynamiccolumn_id numeric,
    meeting_id numeric
);


--
-- Name: passwordreset; Type: TABLE; Schema: public;
--

CREATE TABLE passwordreset (
    id numeric NOT NULL,
    date timestamp without time zone,
    resetconfimation character varying(255),
    employee_id numeric NOT NULL
);


--
-- Name: category_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: client_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: clientdynamicvalue_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY clientdynamicvalue
    ADD CONSTRAINT clientdynamicvalue_pkey PRIMARY KEY (id);


--
-- Name: company_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);


--
-- Name: contact_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- Name: dynamiccolumn_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY dynamiccolumn
    ADD CONSTRAINT dynamiccolumn_pkey PRIMARY KEY (id);


--
-- Name: employee_email_key; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY employee
    ADD CONSTRAINT employee_email_key UNIQUE (email);


--
-- Name: employee_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (id);


--
-- Name: meeting_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (id);


--
-- Name: meetingdynamicvalue_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY meetingdynamicvalue
    ADD CONSTRAINT meetingdynamicvalue_pkey PRIMARY KEY (id);


--
-- Name: passwordreset_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY passwordreset
    ADD CONSTRAINT passwordreset_pkey PRIMARY KEY (id);


--
-- Name: category_company_idx; Type: INDEX; Schema: public;
--

CREATE INDEX category_company_idx ON category USING btree (company_id);


--
-- Name: client_company_idx; Type: INDEX; Schema: public;
--

CREATE INDEX client_company_idx ON client USING btree (company_id);


--
-- Name: firstnameidx; Type: INDEX; Schema: public;
--

CREATE INDEX firstnameidx ON client USING btree (firstname);


--
-- Name: lastnameidx; Type: INDEX; Schema: public;
--

CREATE INDEX lastnameidx ON client USING btree (lastname);


--
-- Name: middlenameidx; Type: INDEX; Schema: public;
--

CREATE INDEX middlenameidx ON client USING btree (middlename);


--
-- Name: notificationstatusidx; Type: INDEX; Schema: public;
--

CREATE INDEX notificationstatusidx ON meeting USING btree (notificationstatus);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--


INSERT INTO hibernate_unique_key(next_hi)    VALUES (1);