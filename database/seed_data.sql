--Run this on primary db only in application.properties
DROP TABLE TENANT;
CREATE TABLE TENANT (id INT AUTO_INCREMENT NOT NULL,
                   name VARCHAR NOT NULL,
                   default_tenant BOOLEAN,
                   driver_class_name VARCHAR,
                   url VARCHAR,
                   username VARCHAR,
                   password VARCHAR,
                   separate_database BOOLEAN,
                   CONSTRAINT PK_TENANT PRIMARY KEY (id));

INSERT INTO TENANT(NAME, DEFAULT_TENANT, DRIVER_CLASS_NAME, URL, USERNAME, PASSWORD, SEPARATE_DATABASE)
VALUES('got', true, 'org.h2.Driver', 'jdbc:h2:~/gotTenantDB;MVCC=TRUE;LOCK_TIMEOUT=10000;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE', 'sa', '', true);
INSERT INTO TENANT(NAME, DEFAULT_TENANT, DRIVER_CLASS_NAME, URL, USERNAME, PASSWORD, SEPARATE_DATABASE)
VALUES('seinfeld', false, 'org.h2.Driver', 'jdbc:h2:~/seinfeldTenantDB;MVCC=TRUE;LOCK_TIMEOUT=10000;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE', 'sa', '', false);
INSERT INTO TENANT(NAME, DEFAULT_TENANT, DRIVER_CLASS_NAME, URL, USERNAME, PASSWORD, SEPARATE_DATABASE)
VALUES('breakingBad', false,'org.h2.Driver', 'jdbc:h2:~/breakingBadTenantDB;MVCC=TRUE;LOCK_TIMEOUT=10000;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE', 'sa', '', true);

--Run this on all tenant db's in application.properties
CREATE TABLE character (id INT AUTO_INCREMENT NOT NULL,
                   name VARCHAR NOT NULL,
                   tenant_id VARCHAR NOT NULL,
                   CONSTRAINT PK_CHARACTER PRIMARY KEY (id));


--Insert GoT and Seinfeld values in primary Tenant DB, which is currently the 'got' one
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