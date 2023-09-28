-- V2__create-tables-product-category.sql

-- Criação da tabela "TB_PRODUCT"
CREATE TABLE TB_PRODUCT (
    id UUID PRIMARY KEY,
    name VARCHAR(70) NOT NULL,
    description VARCHAR(130) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    active BOOLEAN NOT NULL,
    category_id UUID,
    image VARCHAR(64)
);

-- Criação da tabela "TB_CATEGORY"
CREATE TABLE TB_CATEGORY (
    id UUID PRIMARY KEY,
    name VARCHAR(70) NOT NULL,
    description VARCHAR(130) NOT NULL,
    active BOOLEAN NOT NULL
);

-- Adição da chave estrangeira para a coluna "category_id"
ALTER TABLE TB_PRODUCT
ADD CONSTRAINT fk_category
FOREIGN KEY (category_id)
REFERENCES TB_CATEGORY(id);

