DROP DATABASE IF EXISTS db_supermercado;
CREATE DATABASE db_supermercado;
USE db_supermercado;

CREATE TABLE produtos (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE, -- UNIQUE para segurança total no banco
  preco DECIMAL(10,2) NOT NULL,
  quantidade INT NOT NULL
);

CREATE TABLE usuarios (
  cpf VARCHAR(14) NOT NULL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  is_admin TINYINT(1) NOT NULL
);

INSERT INTO usuarios (cpf, nome, is_admin) VALUES ('123', 'adm', 1);