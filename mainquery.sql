drop database if exists `Acme-Chorbies`;
create database `Acme-Chorbies`;
grant select, insert, update, delete
on `Acme-Chorbies`.* to 'acme-user'@'%';
grant select, insert, update, delete, create, drop, references, index, alter,
create temporary tables, lock tables, create view, create routine,
alter routine, execute, trigger, show view
on `Acme-Chorbies`.* to 'acme-manager'@'%';
