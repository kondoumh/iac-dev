Trino
====================

[Distributed SQL query engine for big data](https://trino.io/)

[Trino documentation — Trino 451 Documentation](https://trino.io/docs/current/)

## Deployment

[Deploying Trino — Trino 451 Documentation](https://trino.io/docs/current/installation/deployment.html)


https://trino.io/docs/current/installation/deployment.html#java-runtime-environment

> Trino requires a 64-bit version of Java 22, with a minimum required version of 22.0.0. Earlier versions such as Java 8, Java 11, Java 17 or Java 21 do not work. Newer versions such as Java 23 are not supported – they may work, but are not tested.

## Docker

```shell
docker compose up
```

Run Trino CLI

```shell
docker compose exec -it trino trino
```

Execute query

```shell
trino> select count(*) from postgresql.bm.user;
 _col0 
-------
 50000 
(1 row)

Query 20240711_065651_00006_8wfbu, FINISHED, 1 node
Splits: 1 total, 1 done (100.00%)
0.16 [1 rows, 0B] [6 rows/s, 0B/s]
```

## Kubernetes

```shell
helm repo add trino https://trinodb.github.io/charts
```

```shell
helm install -f values.yml example-trino-cluster trino/trino
```

## Trino CLI

install

```shel
curl -LO https://repo1.maven.org/maven2/io/trino/trino-cli/452/trino-cli-452-executable.jar
mv trino-cli-452-executable.jar trino
chmod +x trino
```

execute Trino

```shell
 ./trino --server http://localhost:8080
```
