# CRaC Console app example

## Manualy create snapshot
```shell
docker build . -t crac-console-xml
```

```shell
docker run -it --privileged --rm --name=crac-console-xml crac-console-xml
```

```shell
java -XX:CRaCCheckpointTo=/opt/crac-files -jar /opt/app/app.jar
```

```shell
docker exec -it -u root crac-console-xml /bin/bash
```

```shell
jcmd /opt/app/app.jar JDK.checkpoint
```

```shell
CONTAINER_ID=$(docker ps | grep crac-console-xml | awk '{print $1}')
docker commit $CONTAINER_ID crac-console-xml:checkpoint
```

```shell
docker run -it --privileged --rm --name=crac-console-xml crac-console-xml:checkpoint java -XX:CRaCRestoreFrom=/opt/crac-files
```
