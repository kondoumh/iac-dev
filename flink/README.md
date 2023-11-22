# Flink playground

[Flink Operations Playground](https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/try-flink/flink-operations-playground/)


## Setup
Build operations playground

```shell
git clone https://github.com/apache/flink-playgrounds.git
cd flink-playgrounds/operations-playground
docker compose build
```

Start the playground
```shell
docker compose up -d
```

Inspect running containers

```shsll
docker compose ps
NAME                                           IMAGE                                                 COMMAND                                                                                                                                                                                        SERVICE                CREATED         STATUS         PORTS
operations-playground-clickevent-generator-1   apache/flink-ops-playground:1-FLINK-1.16-scala_2.12   "/docker-entrypoint.sh java -classpath /opt/ClickCountJob.jar:/opt/flink/lib/* org.apache.flink.playgrounds.ops.clickcount.ClickEventGenerator --bootstrap.servers kafka:9092 --topic input"   clickevent-generator   7 minutes ago   Up 7 minutes   6123/tcp, 8081/tcp
operations-playground-jobmanager-1             apache/flink:1.16.0-scala_2.12-java11                 "/docker-entrypoint.sh jobmanager.sh start-foreground"                                                                                                                                         jobmanager             7 minutes ago   Up 7 minutes   6123/tcp, 0.0.0.0:8081->8081/tcp, :::8081->8081/tcp
operations-playground-kafka-1                  wurstmeister/kafka:2.13-2.8.1                         "start-kafka.sh"                                                                                                                                                                               kafka                  7 minutes ago   Up 7 minutes   0.0.0.0:9092->9092/tcp, :::9092->9092/tcp, 0.0.0.0:9094->9094/tcp, :::9094->9094/tcp
operations-playground-taskmanager-1            apache/flink:1.16.0-scala_2.12-java11                 "/docker-entrypoint.sh taskmanager.sh start-foreground"                                                                                                                                        taskmanager            7 minutes ago   Up 7 minutes   6123/tcp, 8081/tcp
operations-playground-zookeeper-1              wurstmeister/zookeeper:3.4.6                          "/bin/sh -c '/usr/sbin/sshd && start-zk.sh'"                                                                                                                                                   zookeeper              7 minutes ago   Up 7 minutes   22/tcp, 2181/tcp, 2888/tcp, 3888/tcp
```


## Web UI

`http://localhost:8081/`


## Flink CLI

Run Flink CLI

```shell
docker compose run --no-deps client flink --help
```

List jobs

```shell
docker compose run --no-deps client flink list
```

## Stop the playground

```shell
docker compose down -v
```