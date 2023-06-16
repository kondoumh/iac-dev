# CRaC Spring Boot Trial

## Manualy create snapshot
```shell
docker build . -t crac-spring-boot-trial
```

```shell
docker run -it --privileged --rm -p 5001:5001 --name=crac-spring-boot-trial crac-spring-boot-trial
```

```shell
java -XX:CRaCCheckpointTo=/opt/crac-files -jar /opt/app/app.jar
```

```shell
$ curl localhost:5001
Hello! You are visitor number 1.
$ curl localhost:5001
Hello! You are visitor number 2.
$ curl localhost:5001
Hello! You are visitor number 3.
$ curl localhost:5001
Hello! You are visitor number 4.
$ curl localhost:5001
Hello! You are visitor number 5.
```

```shell
docker exec -it -u root crac-spring-boot-trial /bin/bash
```

```shell
$ jps
74 Jps
14 app.jar

jcmd /opt/app/app.jar JDK.checkpoint
```

```shell
$ ls /opt/crac-files/
core-10.img core-14.img ...
```

```shell
CONTAINER_ID=$(docker ps | grep crac | awk '{print $1}')
docker commit $CONTAINER_ID crac-spring-boot-trial:checkpoint
```

```shell
docker run -it --privileged --rm -p 5001:5001 --name=crac-spring-boot-trial crac-spring-boot-trial:checkpoint java -XX:CRaCRestoreFrom=/opt/crac-files
```

