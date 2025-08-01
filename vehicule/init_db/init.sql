--
-- PostgreSQL database dump
--

-- Dumped from database version 14.17 (Ubuntu 14.17-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.17 (Ubuntu 14.17-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: images; Type: TABLE; Schema: public; Owner: group10
--

CREATE TABLE public.images (
    id bigint NOT NULL,
    chemin character varying(255),
    nom character varying(255),
    vehicule_id uuid NOT NULL
);


ALTER TABLE public.images OWNER TO group10;

--
-- Name: images_id_seq; Type: SEQUENCE; Schema: public; Owner: group10
--

ALTER TABLE public.images ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: jwt; Type: TABLE; Schema: public; Owner: group10
--

CREATE TABLE public.jwt (
    id bigint NOT NULL,
    desactive boolean NOT NULL,
    expire boolean NOT NULL,
    valeur character varying(255),
    refresh_token_id bigint,
    utilisateur_id uuid
);


ALTER TABLE public.jwt OWNER TO group10;

--
-- Name: jwt_id_seq; Type: SEQUENCE; Schema: public; Owner: group10
--

ALTER TABLE public.jwt ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.jwt_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: jwt_seq; Type: SEQUENCE; Schema: public; Owner: group10
--

CREATE SEQUENCE public.jwt_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.jwt_seq OWNER TO group10;

--
-- Name: refresh_token; Type: TABLE; Schema: public; Owner: group10
--

CREATE TABLE public.refresh_token (
    id bigint NOT NULL,
    create_at timestamp(6) without time zone NOT NULL,
    creation timestamp(6) with time zone,
    expiration timestamp(6) with time zone,
    expire boolean NOT NULL,
    update_at timestamp(6) without time zone,
    valeur character varying(255)
);


ALTER TABLE public.refresh_token OWNER TO group10;

--
-- Name: refresh_token_id_seq; Type: SEQUENCE; Schema: public; Owner: group10
--

ALTER TABLE public.refresh_token ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.refresh_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: refresh_token_seq; Type: SEQUENCE; Schema: public; Owner: group10
--

CREATE SEQUENCE public.refresh_token_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.refresh_token_seq OWNER TO group10;

--
-- Name: users; Type: TABLE; Schema: public; Owner: group10
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    actif boolean NOT NULL,
    create_at timestamp(6) without time zone NOT NULL,
    delete_at timestamp(6) without time zone,
    email character varying(255),
    password character varying(255) NOT NULL,
    update_at timestamp(6) without time zone,
    username character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO group10;

--
-- Name: vehicule; Type: TABLE; Schema: public; Owner: group10
--

CREATE TABLE public.vehicule (
    id uuid NOT NULL,
    chemin_vers_image character varying(255),
    make character varying(255),
    model character varying(255),
    registration_number character varying(255) NOT NULL,
    rental_price double precision NOT NULL,
    anee integer
);


ALTER TABLE public.vehicule OWNER TO group10;

--
-- Name: vehicule_images; Type: TABLE; Schema: public; Owner: group10
--

CREATE TABLE public.vehicule_images (
    vehicule_id uuid NOT NULL,
    images_id bigint NOT NULL
);


ALTER TABLE public.vehicule_images OWNER TO group10;

--
-- Data for Name: images; Type: TABLE DATA; Schema: public; Owner: group10
--

COPY public.images (id, chemin, nom, vehicule_id) FROM stdin;
1	images/vehiculesUntitled5.png	IMG0null	dfb30a12-ce15-4791-a53e-290e1dda4c61
2	images/vehicules/Untitled5.png	\N	dfb30a12-ce15-4791-a53e-290e1dda4c61
3	images/vehicules/Untitled5.png	\N	dfb30a12-ce15-4791-a53e-290e1dda4c61
4	images/vehicules/IMG-20250526-WA0005.jpg	\N	e279ee3c-68b9-4df0-86c8-6e013160cabe
5	images/vehicules/IMG-20250526-WA0005.jpg	\N	4bd562b5-8eea-41c1-93be-4158dff7fb8c
\.


--
-- Data for Name: jwt; Type: TABLE DATA; Schema: public; Owner: group10
--

COPY public.jwt (id, desactive, expire, valeur, refresh_token_id, utilisateur_id) FROM stdin;
6	f	f	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFwb0BnbWFpbC5jb20iLCJpYXQiOjE3NDg4Mjg4NTYsImV4cCI6MTc0ODgyOTc1Nn0.DSAT8C7FdGzmrXhF-xeGKRzD5L6uZMiZZTg34K8ZWLWw8ehfO-9jY2Y7x5XPL3HcRapw7XRmgaMjsDuEpNlGeQ	6	0dd931d4-303d-418d-accf-31d04f7cb340
7	f	f	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFwb0BnbWFpbC5jb20iLCJpYXQiOjE3NDg4MzA1ODksImV4cCI6MTc0ODgzMTQ4OX0.caKQ8rLngiqyfMcSxkORoMIC92Vq2FL8dtBNOs5tsp-CW_p0AgvS8ZAd89gncS7DC1GZf2xu2Hcj6RayJqaDHQ	7	0dd931d4-303d-418d-accf-31d04f7cb340
\.


--
-- Data for Name: refresh_token; Type: TABLE DATA; Schema: public; Owner: group10
--

COPY public.refresh_token (id, create_at, creation, expiration, expire, update_at, valeur) FROM stdin;
1	2025-06-02 01:42:01.658766	2025-06-02 01:42:01.608002+01	2025-06-17 01:42:01.608009+01	f	2025-06-02 01:42:01.658808	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFAZ21haWwuY29tIiwianRpIjoiNzVlMDM5YWMtZjYyOC00MTA4LWI2ZmYtNGI2YmM3NmMyYzQ4IiwiaWF0IjoxNzQ4ODI0OTIxLCJleHAiOjE3NTAxMjA5MjF9._ukt-TBXmZsyVk2srOfcwvocrjeGqhiBzg2CuDXzYSZEmNw5eCBFuhEZfvKb3doCLLWpeEEorEFJKAM8iHtKEA
2	2025-06-02 01:46:25.669882	2025-06-02 01:46:25.66744+01	2025-06-17 01:46:25.667443+01	f	2025-06-02 01:46:25.669928	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFAZ21haWwuY29tIiwianRpIjoiMGQ1NjUyMjAtOTViNy00ODdmLThmNjItZTI4YzIwMDI5ZmMzIiwiaWF0IjoxNzQ4ODI1MTg1LCJleHAiOjE3NTAxMjExODV9.KphKJLvfBFI50BZ16j-j-8XWVLNL_NaWZW8PC0E2d5r6kPlV399pOnWmg38DsGaUCYm8gqWE0pYTLMX7QEQG2A
3	2025-06-02 01:54:19.041786	2025-06-02 01:54:19.03903+01	2025-06-17 01:54:19.039034+01	f	2025-06-02 01:54:19.041843	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFAZ21haWwuY29tIiwianRpIjoiOTMzYmViYWYtMTRmZi00MjM2LWI3NDctODc3ZjZkM2RjMmVkIiwiaWF0IjoxNzQ4ODI1NjU5LCJleHAiOjE3NTAxMjE2NTl9.zIKuw9fA1SGZlVuSnEb4MxbNNGkkARzntbByrJ90GhPDNm4Zawf9-sHzK-xeoiIb4eF-Jj3l6XYoBr-QvXHBKw
4	2025-06-02 02:33:03.634108	2025-06-02 02:33:03.571236+01	2025-06-17 02:33:03.571243+01	f	2025-06-02 02:33:03.634153	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFwb0BnbWFpbC5jb20iLCJqdGkiOiJmNDhmODI5MC0zZWJjLTRiYmUtOTk5Ny03MTdmODY1NDI3OTMiLCJpYXQiOjE3NDg4Mjc5ODMsImV4cCI6MTc1MDEyMzk4M30.eCpa7yPC4M-Qn593tnKqzr8n1rwziLj809dGwpMTuPaMyAAuZDVI-DsQloWr9O67nIPBzShMkg011hNmRZRkaA
5	2025-06-02 02:43:32.413052	2025-06-02 02:43:32.346641+01	2025-06-17 02:43:32.346648+01	f	2025-06-02 02:43:32.413149	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFwb0BnbWFpbC5jb20iLCJqdGkiOiJkZDljMmY0Mi1kMmQ5LTQxNDUtYTAyZC1kM2I0N2RmYTY3OTIiLCJpYXQiOjE3NDg4Mjg2MTIsImV4cCI6MTc1MDEyNDYxMn0.ACXoULFYr3EEds_I18JqJdBBqUUEHX5ozbv6Px-MiFiRRaMSezZrCai0TRab-9z3A_sIgi03LXzKTOij7Xq2_g
6	2025-06-02 02:47:36.639852	2025-06-02 02:47:36.637529+01	2025-06-17 02:47:36.637532+01	f	2025-06-02 02:47:36.639895	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFwb0BnbWFpbC5jb20iLCJqdGkiOiJmNGNiZTdmOC05MjMzLTQ5NDgtOGJkMy1hZGU1NTgzMWE3ZTIiLCJpYXQiOjE3NDg4Mjg4NTYsImV4cCI6MTc1MDEyNDg1Nn0.1X2a2H0qTmOqd9fDll0BS3LmDFx-AZphf-dcwoR_5DrotUTrBHBC1A4yochUhhd8OqNArg1c5Cb_2oOUacRGZw
7	2025-06-02 03:16:29.977265	2025-06-02 03:16:29.889545+01	2025-06-17 03:16:29.889552+01	f	2025-06-02 03:16:29.977361	eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5heW9tYmFwb0BnbWFpbC5jb20iLCJqdGkiOiI2OWU2ZTdkNy0wZThhLTQ2NWMtOWM5Ni04YzM1ODgwM2JlNzciLCJpYXQiOjE3NDg4MzA1ODksImV4cCI6MTc1MDEyNjU4OX0.dGATWND52cKPInFznHa1qlqcLFvkVmBK458LNgJmgWeNnxt0IUYrJs9iZ20xXknWj1UlZVduYG6TvLsGLmrcWw
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: group10
--

COPY public.users (id, actif, create_at, delete_at, email, password, update_at, username) FROM stdin;
a8d8e2d4-1cc2-4f5d-8167-04f724503f2e	f	2025-05-29 12:12:41.899556	\N	dan@gmail.com	$2a$10$ZgchrZx3t7RDu8fnkN6PEuGlXlry81PQEyWgUDQOR.gd9A3L7J1UW	2025-05-29 12:12:41.899625	DAB
654f9b70-3f2f-42e6-b242-48405457baac	f	2025-05-29 12:33:59.771763	\N	danilo@gmail.com	$2a$10$vc8.TB06MSa8SFNfLVFhF.tgms.FnwQPk40ux0Bc1B6KJixcQzkVa	2025-05-29 12:33:59.771843	DAN
42d8e1fb-5347-4812-bc1c-fe1efc9da680	f	2025-06-02 01:41:22.980811	\N	danayomba@gmail.com	$2a$10$5NO82Yo928OZxBZMk41MyO25PyobrtpH9BIISIfeJW3H5owQsjxgK	2025-06-02 01:41:22.980889	STRING
0dd931d4-303d-418d-accf-31d04f7cb340	f	2025-06-02 02:32:35.264746	\N	danayombapo@gmail.com	$2a$10$u5Rz/EZ2bDBxIXKbgZp5zuu0HSNzofx8BuKZJPUkt2bScfqPu3Aou	2025-06-02 02:32:35.264815	DYVOC
\.


--
-- Data for Name: vehicule; Type: TABLE DATA; Schema: public; Owner: group10
--

COPY public.vehicule (id, chemin_vers_image, make, model, registration_number, rental_price, anee) FROM stdin;
dfb30a12-ce15-4791-a53e-290e1dda4c61	images/vehicules/Untitled5.png	toyota	tuyuta1578	ABC14898	1000	2020
e279ee3c-68b9-4df0-86c8-6e013160cabe	images/vehicules/IMG-20250526-WA0005.jpg	Toyota	147Toyo	1478992	20000	2023
4bd562b5-8eea-41c1-93be-4158dff7fb8c	images/vehicules/IMG-20250526-WA0005.jpg	Toyota	147Toyopo	78992	2000000	2023
\.


--
-- Data for Name: vehicule_images; Type: TABLE DATA; Schema: public; Owner: group10
--

COPY public.vehicule_images (vehicule_id, images_id) FROM stdin;
\.


--
-- Name: images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: group10
--

SELECT pg_catalog.setval('public.images_id_seq', 5, true);


--
-- Name: jwt_id_seq; Type: SEQUENCE SET; Schema: public; Owner: group10
--

SELECT pg_catalog.setval('public.jwt_id_seq', 1, true);


--
-- Name: jwt_seq; Type: SEQUENCE SET; Schema: public; Owner: group10
--

SELECT pg_catalog.setval('public.jwt_seq', 7, true);


--
-- Name: refresh_token_id_seq; Type: SEQUENCE SET; Schema: public; Owner: group10
--

SELECT pg_catalog.setval('public.refresh_token_id_seq', 1, false);


--
-- Name: refresh_token_seq; Type: SEQUENCE SET; Schema: public; Owner: group10
--

SELECT pg_catalog.setval('public.refresh_token_seq', 7, true);


--
-- Name: images images_pkey; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_pkey PRIMARY KEY (id);


--
-- Name: jwt jwt_pkey; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.jwt
    ADD CONSTRAINT jwt_pkey PRIMARY KEY (id);


--
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);


--
-- Name: vehicule_images uke57uhl6x6a5iev1e2m3g9h6af; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.vehicule_images
    ADD CONSTRAINT uke57uhl6x6a5iev1e2m3g9h6af UNIQUE (images_id);


--
-- Name: vehicule uklqve40ggg292m3f7t13kigpci; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.vehicule
    ADD CONSTRAINT uklqve40ggg292m3f7t13kigpci UNIQUE (registration_number);


--
-- Name: jwt ukt9dj65f9n8rla8c14x5lsv2ll; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.jwt
    ADD CONSTRAINT ukt9dj65f9n8rla8c14x5lsv2ll UNIQUE (refresh_token_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: vehicule vehicule_pkey; Type: CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.vehicule
    ADD CONSTRAINT vehicule_pkey PRIMARY KEY (id);


--
-- Name: vehicule_images fk1rpj7t1lxkrafpwd3lvdve3b7; Type: FK CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.vehicule_images
    ADD CONSTRAINT fk1rpj7t1lxkrafpwd3lvdve3b7 FOREIGN KEY (images_id) REFERENCES public.images(id);


--
-- Name: jwt fk5x5slihm7dnfvkhsuw4o3c94o; Type: FK CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.jwt
    ADD CONSTRAINT fk5x5slihm7dnfvkhsuw4o3c94o FOREIGN KEY (refresh_token_id) REFERENCES public.refresh_token(id);


--
-- Name: vehicule_images fko3400ac44xbsi74nhvv7kvwkj; Type: FK CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.vehicule_images
    ADD CONSTRAINT fko3400ac44xbsi74nhvv7kvwkj FOREIGN KEY (vehicule_id) REFERENCES public.vehicule(id);


--
-- Name: jwt fkowcjt2bsvexgfk617r9viwrha; Type: FK CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.jwt
    ADD CONSTRAINT fkowcjt2bsvexgfk617r9viwrha FOREIGN KEY (utilisateur_id) REFERENCES public.users(id);


--
-- Name: images fkq4uoyualnh7w3e2smtqxyusbd; Type: FK CONSTRAINT; Schema: public; Owner: group10
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT fkq4uoyualnh7w3e2smtqxyusbd FOREIGN KEY (vehicule_id) REFERENCES public.vehicule(id);


--
-- PostgreSQL database dump complete
--

