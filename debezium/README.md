# Debezium connector for PostgreSQL

## Homebrew
### Setup PostgreSQL

```
brew install postgresql@11
```

Set wal_level to `logical`

```
vi /usr/local/var/postgresql@11/postgresql.conf
```

```
wal_level = logical
```

Start postgresql service

```
brew services start postgresql@11
```

Add path to psql in ~/.bash_profile or ~/.zshenv

```sh
export PATH=${PATH}:/usr/local/Cellar/postgresql@11/11.12/bin
```

Create database

```
psql postgres
postgres=# create user postgres SUPERUSER;
psql postgres
postgres=# create database hoge owner=postgres;
psql hoge -U postgres
hoge=# CREATE TABLE account(id varchar(8), name varchar(8));
```

### Install Kafka with Zookeeper and start services
[Kafka setup](../kafka/README.md)

### Install debezium

https://debezium.io/documentation/reference/install.html

Download debezium connector archive and extranted its contents to path kafka plugins

```
mkdir -p /usr/local/share/kafka/plugins
curl -L -o debezium-connector-postgres.tar.gz https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.6.1.Final/debezium-connector-postgres-1.6.1.Final-plugin.tar.gz
tar xfz debezium-connector-postgres.tar.gz
mv debezium-connector-postgres /usr/local/share/kafka/plugins
```

Edit connect-distributed.properties

```
vi /usr/local/etc/kafka/connect-distributed.properties
```

Specify `plugin.path`

```
plugin.path=/usr/local/share/kafka/plugins
```

Start connector

```
connect-distributed /usr/local/etc/kafka/connect-distributed.properties
```

```
$ curl http://localhost:8083/connector-plugins | jq
[
  {
    "class": "io.debezium.connector.postgresql.PostgresConnector",
    "type": "source",
    "version": "1.6.1.Final"
  },
  {
    "class": "org.apache.kafka.connect.file.FileStreamSinkConnector",
    "type": "sink",
    "version": "2.8.0"
  },
  {
    "class": "org.apache.kafka.connect.file.FileStreamSourceConnector",
    "type": "source",
    "version": "2.8.0"
  },
  {
    "class": "org.apache.kafka.connect.mirror.MirrorCheckpointConnector",
    "type": "source",
    "version": "1"
  },
  {
    "class": "org.apache.kafka.connect.mirror.MirrorHeartbeatConnector",
    "type": "source",
    "version": "1"
  },
  {
    "class": "org.apache.kafka.connect.mirror.MirrorSourceConnector",
    "type": "source",
    "version": "1"
  }
]
```

Register postgresql connection setting

```
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-postgres.json
```

You need add `plugin.name` with value `pgoutput` to setting

Subscribe topic `connect-status`, then you can get status of connectors

Subscribe topic `localhost.public.account`, then you can get payload of database update

Delete Connector

```
curl -X DELETE http://localhost:8083/connectors/postgres-connector
```

# Docker
https://github.com/debezium/debezium-examples/tree/master/tutorial#using-postgres
