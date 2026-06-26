drop database if exists db_supermercado;
create database db_supermercado;
use db_supermercado;

create table produtos (
id int not null auto_increment primary key,
nome varchar(100) not null unique,
preco decimal(10,2) not null,
quantidade int not null
);

create table usuarios (
cpf varchar(14) not null primary key,
nome varchar(100) not null,
is_admin tinyint(1) not null
);

insert into usuarios (cpf, nome, is_admin) values ('11111111111', 'adm', 1);