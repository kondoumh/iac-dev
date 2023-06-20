# CRaC example-jetty app trial

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
Hello World. 1 times.
$ curl localhost:5001
Hello World. 2 times.
$ curl localhost:5001
Hello World. 3 times.
$ curl localhost:5001
Hello World. 4 times.
$ curl localhost:5001
Hello World. 5 times.
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
core-10.img  core-14.img  core-18.img  core-22.img  core-26.img  core-68.img  cppath        fs-9.img       pagemap-9.img  seccomp.img
core-11.img  core-15.img  core-19.img  core-23.img  core-27.img  core-69.img  dump4.log     ids-9.img      pages-1.img    stats-dump
core-12.img  core-16.img  core-20.img  core-24.img  core-36.img  core-70.img  fdinfo-2.img  inventory.img  perfdata       timens-0.img
core-13.img  core-17.img  core-21.img  core-25.img  core-67.img  core-9.img   files.img     mm-9.img       pstree.img     tty-info.img
```

```shell
CONTAINER_ID=$(docker ps | grep crac | awk '{print $1}')
docker commit $CONTAINER_ID crac-spring-boot-trial:checkpoint
```

```shell
docker run -it --privileged --rm -p 5001:5001 --name=crac-spring-boot-trial crac-spring-boot-trial:checkpoint java -XX:CRaCRestoreFrom=/opt/crac-files
```

## Create deployment (kind)

```shell
kind load docker-image crac-spring-boot-trial:checkpoint
```

```shell
kubectl apply -f deployment.yml
```

```shell
POD=$(kubectl get pod -l app=example-jetty -o jsonpath="{.items[0].metadata.name}")
kubectl port-forward $POD 5001:5001
```
