--
-- PostgreSQL database dump
--

\restrict GfnJ8GD6TsoiKe4zvttF1L0I0BLVuBis9VHlPyVSRlJWaWdwltkaxdDHnsbXLQf

-- Dumped from database version 17.9 (Debian 17.9-1.pgdg13+1)
-- Dumped by pg_dump version 17.9 (Debian 17.9-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
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
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- Name: posts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.posts (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    content text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.posts OWNER TO postgres;

--
-- Name: posts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.posts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.posts_id_seq OWNER TO postgres;

--
-- Name: posts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.posts_id_seq OWNED BY public.posts.id;


--
-- Name: subscriptions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subscriptions (
    follower_id bigint NOT NULL,
    following_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_self_follow CHECK ((follower_id <> following_id))
);


ALTER TABLE public.subscriptions OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    username character varying(50),
    password character varying(100),
    role character varying(20) DEFAULT 'ROLE_USER'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: posts id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts ALTER COLUMN id SET DEFAULT nextval('public.posts_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	Create users table	SQL	V1__Create_users_table.sql	641713181	postgres	2026-03-25 06:53:32.857606	62	t
2	2	Create posts table	SQL	V2__Create_posts_table.sql	-1211158245	postgres	2026-03-25 06:53:32.978114	37	t
3	3	add auth to users	SQL	V3__add_auth_to_users.sql	-860456590	postgres	2026-03-25 06:53:33.041397	19	t
4	4	Create subscriptions  table	SQL	V4__Create_subscriptions _table.sql	-767720677	postgres	2026-03-25 06:53:33.089546	32	t
5	5	add audit columns	SQL	V5__add_audit_columns.sql	635820297	postgres	2026-03-25 06:53:33.135432	9	t
6	6	Add indexes	SQL	V6__Add_indexes.sql	2143552015	postgres	2026-03-25 06:53:33.161696	34	t
7	7	Add username index for security	SQL	V7__Add_username_index_for_security.sql	1810604806	postgres	2026-03-25 11:34:43.460691	95	t
\.


--
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.posts (id, user_id, content, created_at) FROM stdin;
\.


--
-- Data for Name: subscriptions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subscriptions (follower_id, following_id, created_at) FROM stdin;
2	1	2026-03-25 06:55:12.490912
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, email, username, password, role, created_at) FROM stdin;
1	Admin User	admin@example.com	admin	$2a$10$AZg5rtrPgljiIHXzyuel2O4bE5lI2KhL7K08iBej7ykWQqN2Jwcx.	ROLE_ADMIN	2026-03-25 06:53:33.142572
2	Ivan Tech	ivan@example.com	ivan_user	$2a$10$irrnO9TGNOG4EGCGEe9qQ.JzqbPKKKLzdOh9YjFnk5U2zzdR/5C46	ROLE_USER	2026-03-25 06:54:21.966314
3	Artem	artem_2026@example.com	artem123	$2a$10$CHacM1eawz7jTqRLyvYZmuvuDK8zKyihY95W6S0AYiz1HewoEuB2O	ROLE_USER	2026-03-25 13:07:37.954084
4	qwert	qwert@mail	qwer123	$2a$10$JhkDk.XZVrv21QD1CMk.O.W8Yv8Vr9ukaYTlzfM6.IWAus2h3lzzm	ROLE_USER	2026-03-25 11:39:13.924517
6	Newqwert	Newqwert@mail	Newqwer123	$2a$10$o3KwWSHzBMYSZh6JeNaCS.GanceW4uz/81uO0TUb7dya/Q3We7.na	ROLE_USER	2026-03-25 11:45:37.88921
\.


--
-- Name: posts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.posts_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 6, true);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: posts posts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (id);


--
-- Name: subscriptions subscriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscriptions
    ADD CONSTRAINT subscriptions_pkey PRIMARY KEY (follower_id, following_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: idx_posts_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_posts_created_at ON public.posts USING btree (created_at DESC);


--
-- Name: idx_posts_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_posts_user_id ON public.posts USING btree (user_id);


--
-- Name: idx_subscriptions_following_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_subscriptions_following_id ON public.subscriptions USING btree (following_id);


--
-- Name: idx_users_username; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX idx_users_username ON public.users USING btree (username);


--
-- Name: subscriptions fk_follower; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscriptions
    ADD CONSTRAINT fk_follower FOREIGN KEY (follower_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: subscriptions fk_following; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscriptions
    ADD CONSTRAINT fk_following FOREIGN KEY (following_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: posts fk_posts_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict GfnJ8GD6TsoiKe4zvttF1L0I0BLVuBis9VHlPyVSRlJWaWdwltkaxdDHnsbXLQf

