# Debezium connector for PostgreSQL

## Homebrew
Install postgresql

```
brew install postgresql@11
brew services start postgresql@11
psql postgres
postgres=# create user postgres SUPERUSER;
psql postgres
postgres=# create database hoge owner=postgres;
psql hoge -U postgres
hoge=# CREATE TABLE account(id varchar(8), name varchar(8));
```

[Kafka setup](../kafka/README.md)


# Docker
https://github.com/debezium/debezium-examples/tree/master/tutorial#using-postgres
