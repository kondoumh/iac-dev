Kafka cluster experiment
===========================

Deploy a kafka cluster with 3 brokers (default replication factor 2).

```shell
$ kubectl create ns wf
$ helm install -n wf wf-kafka-cluster bitnami/kafka -f kafka-sts/values.yaml
$ kubectl get po,sts -n wf                                                    
NAME                               READY   STATUS    RESTARTS        AGE
pod/wf-kafka-cluster-0             1/1     Running   0               34m
pod/wf-kafka-cluster-1             1/1     Running   0               34m
pod/wf-kafka-cluster-2             1/1     Running   0               34m
pod/wf-kafka-cluster-zookeeper-0   1/1     Running   0               34m

NAME                                          READY   AGE
statefulset.apps/wf-kafka-cluster             3/3     34m
statefulset.apps/wf-kafka-cluster-zookeeper   1/1     34m
```

Deploy a Kafka client pod and run the shell of the kafka client pod.

```shell
kubectl run wf-kafka-client --restart='Never' --image docker.io/bitnami/kafka:2.8.0-debian-10-r0 -n wf --command -- sleep infinity
kubectl -n wf exec --stdin --tty wf-kafka-client -- /bin/bash
```

Check the ID of the running broker.

```shell
$ zookeeper-shell.sh wf-kafka-cluster-zookeeper:2181 ls /brokers/ids
Connecting to wf-kafka-cluster-zookeeper:2181

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[0, 1, 2]
```

