Trino
====================

[Distributed SQL query engine for big data](https://trino.io/)

[Trino documentation — Trino 451 Documentation](https://trino.io/docs/current/)

## Deployment

[Deploying Trino — Trino 451 Documentation](https://trino.io/docs/current/installation/deployment.html)


https://trino.io/docs/current/installation/deployment.html#java-runtime-environment

> Trino requires a 64-bit version of Java 22, with a minimum required version of 22.0.0. Earlier versions such as Java 8, Java 11, Java 17 or Java 21 do not work. Newer versions such as Java 23 are not supported – they may work, but are not tested.

## Docker


## Kubernetes

```shell
helm repo add trino https://trinodb.github.io/charts
```

```shell
helm install -f values.yml example-trino-cluster trino/trino
```