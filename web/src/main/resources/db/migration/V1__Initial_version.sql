--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--


SET search_path = zoo_animals, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: animal; Type: TABLE; Schema: zoo_animals; Owner: fs_schema_owner; Tablespace:
--

CREATE TABLE animal (
    id integer NOT NULL,
    name character varying(255)
);

--
-- Name: animal_id_seq; Type: SEQUENCE; Schema: zoo_animals; Owner: fs_schema_owner
--

CREATE SEQUENCE animal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

             --
-- Name: animal_id_seq; Type: SEQUENCE OWNED BY; Schema: zoo_animals; Owner: fs_schema_owner
--

ALTER SEQUENCE animal_id_seq OWNED BY animal.id;


--
-- Name: id; Type: DEFAULT; Schema: zoo_animals; Owner: fs_schema_owner
--

ALTER TABLE ONLY animal ALTER COLUMN id SET DEFAULT nextval('animal_id_seq'::regclass);


--
-- Name: animal_id_seq; Type: SEQUENCE SET; Schema: zoo_animals; Owner: fs_schema_owner
--

SELECT pg_catalog.setval('animal_id_seq', 178, true);


--
-- Name: animal_pkey; Type: CONSTRAINT; Schema: zoo_animals; Owner: fs_schema_owner; Tablespace:
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_pkey PRIMARY KEY (id);


--
-- Name: uk_qhaa05a0pw9iemm5u02ylep72; Type: CONSTRAINT; Schema: zoo_animals; Owner: fs_schema_owner; Tablespace:
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT uk_qhaa05a0pw9iemm5u02ylep72 UNIQUE (name);

