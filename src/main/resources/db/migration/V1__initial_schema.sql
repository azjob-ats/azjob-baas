CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de usuários com ID como UUID
CREATE TABLE public.tb_user ( 
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
    provider_id VARCHAR(50) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_username UNIQUE (username)
);

-- Tabela de PIN codes
CREATE TABLE public.tb_pin_code (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    code VARCHAR(6) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pin_code_pkey PRIMARY KEY (id),
    CONSTRAINT fk_pin_code_user FOREIGN KEY (user_id) REFERENCES public.tb_user(id)
);

-- Tabela de empresas
CREATE TABLE public.tb_enterprise (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name_enterprise VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,
    CONSTRAINT enterprise_pkey PRIMARY KEY (id),
    CONSTRAINT fk_enterprise_user FOREIGN KEY (user_id) REFERENCES public.tb_user(id)
);

-- Tabela de recrutadores
CREATE TABLE public.tb_recruiter (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name_recruiter VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,
    CONSTRAINT recruiter_pkey PRIMARY KEY (id),
    CONSTRAINT fk_recruiter_user FOREIGN KEY (user_id) REFERENCES public.tb_user(id)
);

-- Tabela de ações
CREATE TABLE public.tb_action (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    CONSTRAINT action_pkey PRIMARY KEY (id)
);

-- Tabela de roles
CREATE TABLE public.tb_role (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT role_pkey PRIMARY KEY (id)
);

-- Tabela de grupos
CREATE TABLE public.tb_group (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    enterprise_id UUID NOT NULL,
    CONSTRAINT group_pkey PRIMARY KEY (id),
    CONSTRAINT fk_group_enterprise FOREIGN KEY (enterprise_id) REFERENCES public.tb_enterprise(id)
);

-- Tabela de permissões
CREATE TABLE public.tb_permission (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    role_id UUID NOT NULL,
    action_id UUID NOT NULL,
    allowed BOOLEAN NOT NULL DEFAULT FALSE,
    enterprise_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    group_id UUID,
    CONSTRAINT permission_pkey PRIMARY KEY (id),
    CONSTRAINT fk_permission_role FOREIGN KEY (role_id) REFERENCES public.tb_role(id),
    CONSTRAINT fk_permission_action FOREIGN KEY (action_id) REFERENCES public.tb_action(id),
    CONSTRAINT fk_permission_enterprise FOREIGN KEY (enterprise_id) REFERENCES public.tb_enterprise(id),
    CONSTRAINT fk_permission_group FOREIGN KEY (group_id) REFERENCES public.tb_group(id)
);

-- Tabela de user_group
CREATE TABLE public.tb_user_group (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    group_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT user_group_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_group_user FOREIGN KEY (user_id) REFERENCES public.tb_user(id),
    CONSTRAINT fk_user_group_group FOREIGN KEY (group_id) REFERENCES public.tb_group(id)
);

-- Índices para melhorar performance
CREATE INDEX idx_pin_code_user_id ON public.tb_pin_code(user_id);
CREATE INDEX idx_pin_code_email ON public.tb_pin_code(email);
CREATE INDEX idx_users_email ON public.tb_user(email);
CREATE INDEX idx_users_username ON public.tb_user(username);
CREATE INDEX idx_enterprise_user_id ON public.tb_enterprise(user_id);
CREATE INDEX idx_recruiter_user_id ON public.tb_recruiter(user_id);
CREATE INDEX idx_group_enterprise_id ON public.tb_group(enterprise_id);
CREATE INDEX idx_permission_role_id ON public.tb_permission(role_id);
CREATE INDEX idx_permission_action_id ON public.tb_permission(action_id);
CREATE INDEX idx_user_group_user_id ON public.tb_user_group(user_id);
CREATE INDEX idx_user_group_group_id ON public.tb_user_group(group_id);

INSERT INTO tb_action (id, name, description) VALUES
(uuid_generate_v4(), 'Create job', 'Create a new job posting'),
(uuid_generate_v4(), 'Edit job', 'Edit an existing job posting'),
(uuid_generate_v4(), 'View job', 'View job details'),
(uuid_generate_v4(), 'Move job between phases', 'Move a job between different Kanban phases'),
(uuid_generate_v4(), 'View applications', 'View candidate applications for a job'),
(uuid_generate_v4(), 'Close job', 'Close a job posting'),
(uuid_generate_v4(), 'Delete job', 'Delete a job posting'),
(uuid_generate_v4(), 'Create/edit Kanban phases', 'Create or edit Kanban board phases'),
(uuid_generate_v4(), 'Access People tab', 'Access the People management section'),
(uuid_generate_v4(), 'Invite/manage users', 'Invite or manage platform users'),
(uuid_generate_v4(), 'Edit company settings', 'Edit organization/company settings'),
(uuid_generate_v4(), 'Access full job history', 'View complete history of a job posting'),
(uuid_generate_v4(), 'View application statistics', 'View statistics about job applications'),
(uuid_generate_v4(), 'Clone job', 'Clone a job to create a similar new one'),
(uuid_generate_v4(), 'Reopen job', 'Reopen a closed job by moving it back to "Backlog"'),
(uuid_generate_v4(), 'Manage jobs and candidates', 'Manage jobs and candidates but not users'),
(uuid_generate_v4(), 'Invite team members', 'Invite team members (analysts, coordinators, external partners)'),
(uuid_generate_v4(), 'Assign specific permissions', 'Assign specific permissions for each action within the organization'),
(uuid_generate_v4(), 'Manage visibility and editing', 'Manage who can view, edit, publish and move jobs');

INSERT INTO tb_role (id, name, description) VALUES
(uuid_generate_v4(), 'Candidate user', 'Standard user who registered and applies for jobs, researches companies'),
(uuid_generate_v4(), 'Anonymous user', 'Unregistered user who can view and apply for jobs, research companies'),
(uuid_generate_v4(), 'Company', 'Company that created an account on the platform'),
(uuid_generate_v4(), 'Manager Recruiter', 'Company person responsible for creating processes and managing people'),
(uuid_generate_v4(), 'Collaborator Recruiter', 'Company person responsible for managing recruitment processes'),
(uuid_generate_v4(), 'Guest', 'Person with access to view recruitment processes'),
(uuid_generate_v4(), 'HR Analyst', 'Responsible for resume screening, interview scheduling and recruitment support'),
(uuid_generate_v4(), 'HR Coordinator', 'Supervises HR team and ensures recruitment follows company policies'),
(uuid_generate_v4(), 'Department Manager', 'Requests new positions, participates in profile definition and approves candidates'),
(uuid_generate_v4(), 'Technical Interviewer', 'Evaluates candidates based on specific technical skills during interviews'),
(uuid_generate_v4(), 'Organizational Psychologist', 'Conducts behavioral assessments and psychological tests'),
(uuid_generate_v4(), 'Talent Acquisition Analyst', 'Responsible for job advertising campaigns and attraction strategies'),
(uuid_generate_v4(), 'External Recruitment Partner', 'Consultant or outsourced company that helps fill specific positions'),
(uuid_generate_v4(), 'Company Account Administrator', 'Has full control of company account, can add users, edit permissions and view reports'),
(uuid_generate_v4(), 'HR Assistant', 'Assists with administrative HR activities like document organization and candidate contact'),
(uuid_generate_v4(), 'Recruitment Assistant', 'Supports resume screening, email sending and interview scheduling'),
(uuid_generate_v4(), 'HR Technician', 'Technical professional who executes recruitment, selection and personnel administration routines'),
(uuid_generate_v4(), 'HR Intern', 'Supports recruitment activities under supervision, participating in screenings and administrative support'),
(uuid_generate_v4(), 'HR Administrative Assistant', 'Focuses on internal controls, registrations and support for selection processes'),
(uuid_generate_v4(), 'Candidate Support Operator', 'Answers questions, confirms attendance and supports candidates in the selection process'),
(uuid_generate_v4(), 'Interview Monitor', 'Schedules, organizes and monitors in-person or online interviews'),
(uuid_generate_v4(), 'Payroll Assistant', 'Supports records and integration of new employees after selection process'),
(uuid_generate_v4(), 'Selection Technician', 'Conducts operational interviews and applies practical tests for base functions'),
(uuid_generate_v4(), 'Internal Communication Assistant', 'Responsible for internally advertising new jobs and recruitment-related communications');