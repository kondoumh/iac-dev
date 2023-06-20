# CRaC Console app example

## Manualy create snapshot
```shell
docker build . -t crac-console-counter
```

```shell
docker run -it --privileged --rm --name=crac-console-counter crac-console-counter
```

```shell
java -XX:CRaCCheckpointTo=/opt/crac-files -jar /opt/app/app.jar
```

```shell
docker exec -it -u root crac-console-counter /bin/bash
```

```shell
jcmd /opt/app/app.jar JDK.checkpoint
```

```shell
CONTAINER_ID=$(docker ps | grep crac-console-counter | awk '{print $1}')
docker commit $CONTAINER_ID crac-console-counter:checkpoint
```

```shell
docker run -it -e DEPLOY_STAGE=container --privileged --rm --name=crac-console-counter crac-console-counter:checkpoint java -XX:CRaCRestoreFrom=/opt/crac-files
```
