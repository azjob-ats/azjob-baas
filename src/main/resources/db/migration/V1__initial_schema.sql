--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5 (Debian 17.5-1.pgdg120+1)

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
-- Name: group_audit_logs; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.group_audit_logs (
    id uuid NOT NULL,
    action character varying(50) NOT NULL,
    ip_address character varying(45),
    new_value jsonb,
    old_value jsonb,
    performed_at timestamp(6) without time zone NOT NULL,
    group_id uuid NOT NULL,
    performed_by uuid NOT NULL,
    target_user_id uuid
);


ALTER TABLE public.group_audit_logs OWNER TO admin;

--
-- Name: group_invitations; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.group_invitations (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    status character varying(20) NOT NULL,
    token character varying(100) NOT NULL,
    group_id uuid NOT NULL,
    invited_by uuid NOT NULL,
    role_id uuid,
    CONSTRAINT group_invitations_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'EXPIRED'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.group_invitations OWNER TO admin;

--
-- Name: group_members; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.group_members (
    id uuid NOT NULL,
    invitation_expires_at timestamp(6) without time zone,
    is_active boolean NOT NULL,
    joined_at timestamp(6) without time zone NOT NULL,
    assigned_role_id uuid,
    group_id uuid NOT NULL,
    invited_by uuid,
    user_id uuid NOT NULL
);


ALTER TABLE public.group_members OWNER TO admin;

--
-- Name: groups; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.groups (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description text,
    is_active boolean NOT NULL,
    name character varying(100) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    default_role_id uuid
);


ALTER TABLE public.groups OWNER TO admin;

--
-- Name: pin_codes; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.pin_codes (
    id uuid NOT NULL,
    code character varying(6) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    is_used boolean NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.pin_codes OWNER TO admin;

--
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.role_permissions (
    role_id uuid NOT NULL,
    permission character varying(255) NOT NULL
);


ALTER TABLE public.role_permissions OWNER TO admin;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.roles (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(255),
    is_default boolean NOT NULL,
    level integer NOT NULL,
    name character varying(100) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.roles OWNER TO admin;

--
-- Name: tb_order_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.tb_order_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tb_order_seq OWNER TO admin;

--
-- Name: users; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    address character varying(255),
    avatar character varying(255),
    bio text,
    birth_date date,
    city character varying(100),
    country character varying(100),
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    email character varying(255) NOT NULL,
    first_name character varying(100) NOT NULL,
    gender character varying(20),
    is_active boolean NOT NULL,
    is_blocked boolean NOT NULL,
    is_verified boolean NOT NULL,
    last_name character varying(100),
    password character varying(255) NOT NULL,
    phone character varying(20),
    state character varying(100),
    time_zone character varying(50),
    updated_at timestamp(6) without time zone NOT NULL,
    username character varying(50),
    zip_code character varying(20),
    role_id uuid
);


ALTER TABLE public.users OWNER TO admin;

--
-- Data for Name: group_audit_logs; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.group_audit_logs (id, action, ip_address, new_value, old_value, performed_at, group_id, performed_by, target_user_id) FROM stdin;
\.


--
-- Data for Name: group_invitations; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.group_invitations (id, created_at, email, expires_at, status, token, group_id, invited_by, role_id) FROM stdin;
\.


--
-- Data for Name: group_members; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.group_members (id, invitation_expires_at, is_active, joined_at, assigned_role_id, group_id, invited_by, user_id) FROM stdin;
\.


--
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.groups (id, created_at, description, is_active, name, updated_at, created_by, default_role_id) FROM stdin;
\.


--
-- Data for Name: pin_codes; Type: TABLE DATA; Schema: public; Owner: admin
--


--
-- Data for Name: role_permissions; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.role_permissions (role_id, permission) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.roles (id, created_at, description, is_default, level, name, updated_at) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: admin
--

--
-- Name: tb_order_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.tb_order_seq', 1, false);


--
-- Name: group_audit_logs group_audit_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_audit_logs
    ADD CONSTRAINT group_audit_logs_pkey PRIMARY KEY (id);


--
-- Name: group_invitations group_invitations_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_invitations
    ADD CONSTRAINT group_invitations_pkey PRIMARY KEY (id);


--
-- Name: group_members group_members_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_members
    ADD CONSTRAINT group_members_pkey PRIMARY KEY (id);


--
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- Name: pin_codes pin_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.pin_codes
    ADD CONSTRAINT pin_codes_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: groups uk_8mf0is8024pqmwjxgldfe54l7; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT uk_8mf0is8024pqmwjxgldfe54l7 UNIQUE (name);


--
-- Name: group_invitations uk_mvxay7s6ih90rl9b5fa66y4de; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_invitations
    ADD CONSTRAINT uk_mvxay7s6ih90rl9b5fa66y4de UNIQUE (token);


--
-- Name: roles uk_ofx66keruapi6vyqpv6f2or37; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_ofx66keruapi6vyqpv6f2or37 UNIQUE (name);


--
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: group_audit_logs fk5hxto0ph6ewi5r2qrxo9xpc0q; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_audit_logs
    ADD CONSTRAINT fk5hxto0ph6ewi5r2qrxo9xpc0q FOREIGN KEY (performed_by) REFERENCES public.users(id);


--
-- Name: group_invitations fk8kvc155725cd849jpmbuqay8c; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_invitations
    ADD CONSTRAINT fk8kvc155725cd849jpmbuqay8c FOREIGN KEY (group_id) REFERENCES public.groups(id);


--
-- Name: groups fki4yyvbs8kcx7fxlhwe75f2nec; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT fki4yyvbs8kcx7fxlhwe75f2nec FOREIGN KEY (default_role_id) REFERENCES public.roles(id);


--
-- Name: groups fkkhpvhy2p2c1un4krvhwnau23b; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT fkkhpvhy2p2c1un4krvhwnau23b FOREIGN KEY (created_by) REFERENCES public.users(id);


--
-- Name: group_members fkkv9vlrye4rmhqjq4qohy2n5a6; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_members
    ADD CONSTRAINT fkkv9vlrye4rmhqjq4qohy2n5a6 FOREIGN KEY (group_id) REFERENCES public.groups(id);


--
-- Name: group_invitations fkm4p4ritm75ijvunl9ehwpulkx; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_invitations
    ADD CONSTRAINT fkm4p4ritm75ijvunl9ehwpulkx FOREIGN KEY (invited_by) REFERENCES public.users(id);


--
-- Name: role_permissions fkn5fotdgk8d1xvo8nav9uv3muc; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT fkn5fotdgk8d1xvo8nav9uv3muc FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: group_members fkniqyx6vxtu3kdvqxqedillyi6; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_members
    ADD CONSTRAINT fkniqyx6vxtu3kdvqxqedillyi6 FOREIGN KEY (assigned_role_id) REFERENCES public.roles(id);


--
-- Name: group_members fknr9qg33qt2ovmv29g4vc3gtdx; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_members
    ADD CONSTRAINT fknr9qg33qt2ovmv29g4vc3gtdx FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: group_audit_logs fko4jj4i6ucbcmlumuj662a96e0; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_audit_logs
    ADD CONSTRAINT fko4jj4i6ucbcmlumuj662a96e0 FOREIGN KEY (target_user_id) REFERENCES public.users(id);


--
-- Name: users fkp56c1712k691lhsyewcssf40f; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: group_invitations fkpmm7nxr11u7mujskh3qc2r02i; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_invitations
    ADD CONSTRAINT fkpmm7nxr11u7mujskh3qc2r02i FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: group_members fkq0st4sk72vscrbjvs89qc1qpu; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_members
    ADD CONSTRAINT fkq0st4sk72vscrbjvs89qc1qpu FOREIGN KEY (invited_by) REFERENCES public.users(id);


--
-- Name: group_audit_logs fkrsog6hog75botrqlychj5meae; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.group_audit_logs
    ADD CONSTRAINT fkrsog6hog75botrqlychj5meae FOREIGN KEY (group_id) REFERENCES public.groups(id);


--
-- Name: pin_codes fktnbkdidrkdqow2xobko9mj2k; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.pin_codes
    ADD CONSTRAINT fktnbkdidrkdqow2xobko9mj2k FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

