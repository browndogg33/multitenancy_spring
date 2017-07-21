--Run this on all db's in application.properties
CREATE TABLE character (id INT AUTO_INCREMENT NOT NULL,
                   name VARCHAR NOT NULL,
                   tenant_id VARCHAR NOT NULL,
                   CONSTRAINT PK_CHARACTER PRIMARY KEY (id));

--Insert GoT and Seinfeld values in primaryDB, which is currently the 'got' one
--GoT Tenant
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Ned Stark', 'got');
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Jamie Lannister', 'got');
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Daenerys Targaryen', 'got');

--Seinfeld Tenant
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Jerry Seinfeld', 'seinfeld');
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('George Costanza', 'seinfeld');
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Elaine Benes', 'seinfeld');
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Kramer', 'seinfeld');

--Insert Breaking Bad values in Breaking Bad database
--Breaking Bad Tenant
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Walter White', 'breakingBad');
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Jesse Pinkman', 'breakingBad');
INSERT INTO CHARACTER(NAME, TENANT_ID) VALUES('Saul Goodman', 'breakingBad');