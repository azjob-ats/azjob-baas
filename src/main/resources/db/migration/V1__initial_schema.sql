CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de usuários com ID como UUID
CREATE TABLE public.users ( 
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    address VARCHAR(255),
    avatar VARCHAR(255),
    bio TEXT,
    birth_date DATE,
    city VARCHAR(100),
    country VARCHAR(100),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    gender VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    last_name VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    state VARCHAR(100),
    time_zone VARCHAR(50),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(50),
    zip_code VARCHAR(20),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    provider VARCHAR(50) NOT NULL,
    id_provider VARCHAR(50) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_username UNIQUE (username)
);

-- Tabela de PIN codes
CREATE TABLE public.pin_codes (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    code VARCHAR(6) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    id_user UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pin_codes_pkey PRIMARY KEY (id),
    CONSTRAINT fk_pin_codes_user FOREIGN KEY (id_user) REFERENCES public.users(id)
);

-- Tabela de empresas
CREATE TABLE public.enterprise (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name_enterprise VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    id_user UUID NOT NULL,
    CONSTRAINT enterprise_pkey PRIMARY KEY (id),
    CONSTRAINT fk_enterprise_user FOREIGN KEY (id_user) REFERENCES public.users(id)
);

-- Tabela de recrutadores
CREATE TABLE public.recruiter (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name_recruiter VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    id_user UUID NOT NULL,
    CONSTRAINT recruiter_pkey PRIMARY KEY (id),
    CONSTRAINT fk_recruiter_user FOREIGN KEY (id_user) REFERENCES public.users(id)
);

-- Tabela de ações
CREATE TABLE public.action (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    CONSTRAINT action_pkey PRIMARY KEY (id)
);

-- Tabela de roles
CREATE TABLE public.role (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT role_pkey PRIMARY KEY (id)
);

-- Tabela de grupos
CREATE TABLE public."group" (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    id_enterprise UUID NOT NULL,
    CONSTRAINT group_pkey PRIMARY KEY (id),
    CONSTRAINT fk_group_enterprise FOREIGN KEY (id_enterprise) REFERENCES public.enterprise(id)
);

-- Tabela de permissões
CREATE TABLE public.permission (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    id_role UUID NOT NULL,
    id_action UUID NOT NULL,
    allowed BOOLEAN NOT NULL DEFAULT FALSE,
    id_enterprise UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    id_group UUID,
    CONSTRAINT permission_pkey PRIMARY KEY (id),
    CONSTRAINT fk_permission_role FOREIGN KEY (id_role) REFERENCES public.role(id),
    CONSTRAINT fk_permission_action FOREIGN KEY (id_action) REFERENCES public.action(id),
    CONSTRAINT fk_permission_enterprise FOREIGN KEY (id_enterprise) REFERENCES public.enterprise(id),
    CONSTRAINT fk_permission_group FOREIGN KEY (id_group) REFERENCES public."group"(id)
);

-- Tabela de user_group
CREATE TABLE public.user_group (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    id_user UUID NOT NULL,
    id_group UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT user_group_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_group_user FOREIGN KEY (id_user) REFERENCES public.users(id),
    CONSTRAINT fk_user_group_group FOREIGN KEY (id_group) REFERENCES public."group"(id)
);

-- Índices para melhorar performance
CREATE INDEX idx_pin_codes_id_user ON public.pin_codes(id_user);
CREATE INDEX idx_pin_codes_email ON public.pin_codes(email);
CREATE INDEX idx_users_email ON public.users(email);
CREATE INDEX idx_users_username ON public.users(username);
CREATE INDEX idx_enterprise_id_user ON public.enterprise(id_user);
CREATE INDEX idx_recruiter_id_user ON public.recruiter(id_user);
CREATE INDEX idx_id_group_enterprise ON public."group"(id_enterprise);
CREATE INDEX idx_permission_id_role ON public.permission(id_role);
CREATE INDEX idx_permission_id_action ON public.permission(id_action);
CREATE INDEX idx_user_id_group_user ON public.user_group(id_user);
CREATE INDEX idx_user_id_group_group ON public.user_group(id_group);

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
('22222222-2222-2222-2222-122222222222', 'Payroll Assistant', 'Supports records and integration of new employees after selection process'),
('23232323-2323-2323-2323-232323232323', 'Selection Technician', 'Conducts operational interviews and applies practical tests for base functions'),
('24242424-2424-2424-2424-242424242424', 'Internal Communication Assistant', 'Responsible for internally advertising new jobs and recruitment-related communications');