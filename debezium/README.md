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

Download debezium connector archive and extract its contents to kafka plugins directory

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
$ curl http://localhost:8083/connector-plugins | jq .
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

You need to add `plugin.name` with value `pgoutput` to setting

[register-postgress.json](https://github.com/kondoumh/iac-dev/blob/master/debezium/register-postgres.json#L13)

Subscribe topic `connect-status`, then you can get status of connectors

Subscribe topic `localhost.public.account`, then you can get payload of database update

Delete Connector

```
curl -X DELETE http://localhost:8083/connectors/postgres-connector
```

# Docker
https://github.com/debezium/debezium-examples/tree/master/tutorial#using-postgres

# Kubernetes

## primitive way
Kafka has been pre-deployed by bitnami's helmchart

Install and execute connector manually in kafka pod

```
kubectl -n wf exec --stdin --tty wf-kafka-cluster-0 -- /bin/bash
cd /opt/bitnami/kafka/config
sed -i -e "s/#plugin.path=/plugin.path=\/opt\/bitnami\/kafka\/plugins/g" connect-distributed.properties
cd /opt/bitnami
mkdir work && cd work
curl https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.6.1.Final/debezium-connector-postgres-1.6.1.Final-plugin.tar.gz | tar xvz
mkdir /opt/bitnami/kafka/plugins
mv debezium-connector-postgres /opt/bitnami/kafka/plugins/
connect-distributed.sh /opt/bitnami/kafka/config/connect-distributed.properties &
```

List plugins

```
$ curl http://localhost:8083/connector-plugins
[{"class":"io.debezium.connector.postgresql.PostgresConnector","type":"source","version":"1.6.1.Final"},{"class":"org.apache.kafka.connect.file.FileStreamSinkConnector","type":"sink","version":"2.8.0"},{"class":"org.apache.kafka.connect.file.FileStreamSourceConnector","type":"source","version":"2.8.0"},{"class":"org.apache.kafka.connect.mirror.MirrorCheckpointConnector","type":"source","version":"1"},{"class":"org.apache.kafka.connect.mirror.MirrorHeartbeatConnector","type":"source","version":"1"},{"class":"org.apache.kafka.connect.mirror.MirrorSourceConnector","type":"source","version":"1"}]
```

Registor connctor

cp json from local
```
kubectl -n wf cp register-postgres-k8s.json wf-kafka-cluster-0:/opt/bitnami/work
```
register
```
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-postgres-k8s.json
```

```sh
kubectl run wf-kafka-client --restart='Never' --image docker.io/bitnami/kafka:2.8.0-debian-10-r0 -n wf --command -- sleep infinity
kubectl exec --tty -i wf-kafka-client -n wf -- bash
# $ kafka-topics.sh --list --zookeeper 10.106.187.156:2181 # cluster-ip
$ kafka-topics.sh --list --zookeeper wf-kafka-cluster-zookeeper:2181
wf-db-postgresql.public.containers

kafka-console-consumer.sh \
    --bootstrap-server wf-kafka-cluster:9092 \
    --topic postgres.public.account \
    --from-beginning
```

## Using debeaium connecter image

https://hub.docker.com/r/debezium/connect
https://github.com/debezium/docker-images
https://ibm-cloud-architecture.github.io/refarch-container-inventory/debezium-postgresql/


```
kubectl apply -f kafka-connect-deploy.yaml -n wf
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:30500/connectors/ -d @register-postgres-k8s.json
```

