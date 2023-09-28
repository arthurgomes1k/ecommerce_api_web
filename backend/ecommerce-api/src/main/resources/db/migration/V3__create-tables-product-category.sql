-- Criação da tabela Endereco
CREATE TABLE TB_ENDERECO (
                             id BIGINT PRIMARY KEY,
                             rua VARCHAR(255),
                             cidade VARCHAR(255)
);

CREATE TABLE TB_USER_ENDERECO (
                                  user_id UUID PRIMARY KEY,
                                  endereco_id BIGINT UNIQUE ,
                                  FOREIGN KEY (user_id) REFERENCES TB_USER(id),
                                  FOREIGN KEY (endereco_id) REFERENCES TB_ENDERECO(id)
);