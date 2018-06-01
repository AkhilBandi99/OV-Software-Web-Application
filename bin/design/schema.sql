CREATE DATABASE OV_payrates;

CREATE TABLE Humres (
res_id integer,
fullname varchar(200),
emp_stat char(20),
freefield_16 varchar(20),
PRIMARY KEY(res_id));

CREATE TABLE Employeerates (
crdnr integer,
purchaseprice double,
vandatum Date,
totdatum Date,
FOREIGN KEY(crdnr) REF Humres(res_id));
