CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE public.pin_codes (
    id uuid NOT NULL,
    code character varying(6) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    is_used boolean NOT NULL,
    user_id uuid NOT NULL,
    is_deleted boolean NOT NULL
);

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
    is_deleted boolean NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)
);

CREATE TABLE public.enterprise (
    id UUID PRIMARY KEY,
    name_enterprise VARCHAR(255) NOT NULL,
    is_deleted boolean NOT NULL,
    id_user UUID NOT NULL REFERENCES public.users(id)
);

CREATE TABLE public.recruiter (
    id UUID PRIMARY KEY,
    name_recruiter VARCHAR(255) NOT NULL,
    is_deleted boolean NOT NULL,
    id_user UUID NOT NULL REFERENCES public.users(id)
);

CREATE TABLE public.action (
    id UUID PRIMARY KEY,
    is_deleted boolean NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE public.role (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_deleted boolean NOT NULL,
    description TEXT
);

CREATE TABLE public."group" (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_deleted boolean NOT NULL,
    id_enterprise UUID NOT NULL REFERENCES public.enterprise(id)
);

CREATE TABLE public.permission (
    id UUID PRIMARY KEY,
    id_role UUID NOT NULL REFERENCES public.role(id),
    id_action UUID NOT NULL REFERENCES public.action(id),
    allowed BOOLEAN NOT NULL DEFAULT FALSE,
    id_enterprise UUID NOT NULL REFERENCES public.enterprise(id),
    is_deleted boolean NOT NULL,
    id_group UUID REFERENCES public."group"(id)
);

CREATE TABLE public.user_group (
    user_id UUID NOT NULL REFERENCES public.users(id),
    group_id UUID NOT NULL REFERENCES public."group"(id),
    is_deleted boolean NOT NULL,
    PRIMARY KEY (user_id, group_id)
);

ALTER TABLE public.pin_codes
    ADD CONSTRAINT pin_codes_pkey PRIMARY KEY (id);

ALTER TABLE public.pin_codes
    ADD CONSTRAINT fktnbkdidrkdqow2xobko9mj2k FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE public.pin_codes OWNER TO admin;
ALTER TABLE public.users OWNER TO admin;

INSERT INTO action (id, name, description) VALUES
('11111111-1111-1111-1111-111111111111', 'Create job', 'Create a new job posting'),
('22222222-2222-2222-2222-222222222222', 'Edit job', 'Edit an existing job posting'),
('33333333-3333-3333-3333-333333333333', 'View job', 'View job details'),
('44444444-4444-4444-4444-444444444444', 'Move job between phases', 'Move a job between different Kanban phases'),
('55555555-5555-5555-5555-555555555555', 'View applications', 'View candidate applications for a job'),
('66666666-6666-6666-6666-666666666666', 'Close job', 'Close a job posting'),
('77777777-7777-7777-7777-777777777777', 'Delete job', 'Delete a job posting'),
('88888888-8888-8888-8888-888888888888', 'Create/edit Kanban phases', 'Create or edit Kanban board phases'),
('99999999-9999-9999-9999-999999999999', 'Access People tab', 'Access the People management section'),
('10101010-1010-1010-1010-101010101010', 'Invite/manage users', 'Invite or manage platform users'),
('11111111-1111-1111-1111-111111111112', 'Edit company settings', 'Edit organization/company settings'),
('12121212-1212-1212-1212-121212121212', 'Access full job history', 'View complete history of a job posting'),
('13131313-1313-1313-1313-131313131313', 'View application statistics', 'View statistics about job applications'),
('14141414-1414-1414-1414-141414141414', 'Clone job', 'Clone a job to create a similar new one'),
('15151515-1515-1515-1515-151515151515', 'Reopen job', 'Reopen a closed job by moving it back to "Backlog"'),
('16161616-1616-1616-1616-161616161616', 'Manage jobs and candidates', 'Manage jobs and candidates but not users'),
('17171717-1717-1717-1717-171717171717', 'Invite team members', 'Invite team members (analysts, coordinators, external partners)'),
('18181818-1818-1818-1818-181818181818', 'Assign specific permissions', 'Assign specific permissions for each action within the organization'),
('19191919-1919-1919-1919-191919191919', 'Manage visibility and editing', 'Manage who can view, edit, publish and move jobs');

INSERT INTO role (id, name, description) VALUES
('11111111-1111-1111-1111-111111111111', 'Candidate user', 'Standard user who registered and applies for jobs, researches companies'),
('22222222-2222-2222-2222-222222222222', 'Anonymous user', 'Unregistered user who can view and apply for jobs, research companies'),
('33333333-3333-3333-3333-333333333333', 'Company', 'Company that created an account on the platform'),
('44444444-4444-4444-4444-444444444444', 'Manager Recruiter', 'Company person responsible for creating processes and managing people'),
('55555555-5555-5555-5555-555555555555', 'Collaborator Recruiter', 'Company person responsible for managing recruitment processes'),
('66666666-6666-6666-6666-666666666666', 'Guest', 'Person with access to view recruitment processes'),
('77777777-7777-7777-7777-777777777777', 'HR Analyst', 'Responsible for resume screening, interview scheduling and recruitment support'),
('88888888-8888-8888-8888-888888888888', 'HR Coordinator', 'Supervises HR team and ensures recruitment follows company policies'),
('99999999-9999-9999-9999-999999999999', 'Department Manager', 'Requests new positions, participates in profile definition and approves candidates'),
('10101010-1010-1010-1010-101010101010', 'Technical Interviewer', 'Evaluates candidates based on specific technical skills during interviews'),
('11111111-1111-1111-1111-111111111112', 'Organizational Psychologist', 'Conducts behavioral assessments and psychological tests'),
('12121212-1212-1212-1212-121212121212', 'Talent Acquisition Analyst', 'Responsible for job advertising campaigns and attraction strategies'),
('13131313-1313-1313-1313-131313131313', 'External Recruitment Partner', 'Consultant or outsourced company that helps fill specific positions'),
('14141414-1414-1414-1414-141414141414', 'Company Account Administrator', 'Has full control of company account, can add users, edit permissions and view reports'),
('15151515-1515-1515-1515-151515151515', 'HR Assistant', 'Assists with administrative HR activities like document organization and candidate contact'),
('16161616-1616-1616-1616-161616161616', 'Recruitment Assistant', 'Supports resume screening, email sending and interview scheduling'),
('17171717-1717-1717-1717-171717171717', 'HR Technician', 'Technical professional who executes recruitment, selection and personnel administration routines'),
('18181818-1818-1818-1818-181818181818', 'HR Intern', 'Supports recruitment activities under supervision, participating in screenings and administrative support'),
('19191919-1919-1919-1919-191919191919', 'HR Administrative Assistant', 'Focuses on internal controls, registrations and support for selection processes'),
('20202020-2020-2020-2020-202020202020', 'Candidate Support Operator', 'Answers questions, confirms attendance and supports candidates in the selection process'),
('21212121-2121-2121-2121-212121212121', 'Interview Monitor', 'Schedules, organizes and monitors in-person or online interviews'),
('22222222-2222-2222-2222-222222222222', 'Payroll Assistant', 'Supports records and integration of new employees after selection process'),
('23232323-2323-2323-2323-232323232323', 'Selection Technician', 'Conducts operational interviews and applies practical tests for base functions'),
('24242424-2424-2424-2424-242424242424', 'Internal Communication Assistant', 'Responsible for internally advertising new jobs and recruitment-related communications');