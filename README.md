# Just Be Honest (JBH) with Kotlin

 JBH[Kotlin/SpringBoot] is the backend component of a self-managed personal finance tracking software. The idea of this personal project is to offer me a comprehensive overview of my financial landscape by allowing manual input and management of financial account data, transfers, and expenses. With a focus on user-driven control, 'Just Be Honest' empowers me to take charge of my financial well-being.


## Swagger

http://localhost:9999/jbh/swagger-ui.html

# Database Migration

## Create DB

```
CREATE DATABASE "JBH" WITH OWNER = jbh_admin ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8' TABLESPACE = pg_default CONNECTION LIMIT = -1;
```
## Create extension

```
CREATE EXTENSION pgcrypto;
```
## Set global variables

```
ALTER DATABASE "JBH" SET TIME ZONE 'America/Bogota';
```

## Remove All Data

```
-- Remove All Data
DELETE FROM transactions;
DELETE FROM accounts;
DELETE FROM sub_categories;
DELETE FROM categories;
DELETE FROM users_group;
DELETE FROM user_roles;
DELETE FROM jbh_user;
```
