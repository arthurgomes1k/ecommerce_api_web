-- V1__create_table_user.sql

-- Criação da tabela "user"
CREATE TABLE TB_USER (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    image VARCHAR(64),
    active BOOLEAN NOT NULL
);

-- Criação da tabela "role"
CREATE TABLE TB_ROLE (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL
);

-- Criação da tabela de relacionamento "user_role"
CREATE TABLE TB_USER_ROLE (
    user_id UUID,
    role_id UUID,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES TB_USER(id),
    FOREIGN KEY (role_id) REFERENCES TB_ROLE(id)
);