Kafka cluster experiment
===========================

Deploy a kafka cluster with 3 brokers.

## Preparation

Kafka chart values.yaml

```yaml
replicaCount: 4
replicationFactor: 2
deleteTopicEnable: true
```

```shell
$ kubectl create ns wf
$ helm install -n wf wf-kafka-cluster bitnami/kafka -f kafka-sts/values.yaml
$ kubectl get po,sts,svc -n wf                                                    
NAME                               READY   STATUS    RESTARTS        AGE
pod/wf-kafka-cluster-0             1/1     Running   0               34m
pod/wf-kafka-cluster-1             1/1     Running   0               34m
pod/wf-kafka-cluster-2             1/1     Running   0               34m
pod/wf-kafka-cluster-zookeeper-0   1/1     Running   0               34m

NAME                                          READY   AGE
statefulset.apps/wf-kafka-cluster             3/3     34m
statefulset.apps/wf-kafka-cluster-zookeeper   1/1     34m

NAME                                  TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
wf-kafka-cluster                      ClusterIP   10.111.185.149   <none>        9092/TCP                     34m
wf-kafka-cluster-headless             ClusterIP   None             <none>        9092/TCP,9093/TCP            34m
wf-kafka-cluster-zookeeper            ClusterIP   10.96.148.71     <none>        2181/TCP,2888/TCP,3888/TCP   34m
wf-kafka-cluster-zookeeper-headless   ClusterIP   None             <none>        2181/TCP,2888/TCP,3888/TCP   34m
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

## Create a topic, then add a broker, and then add partitions to the topic

Create topic.

```shell
$ kafka-topics.sh --create --zookeeper wf-kafka-cluster-zookeeper:2181 --replication-factor 2 --partitions 3 --topic topic-1
Created topic topic-1.
```

Describe created topic.

```
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-1
Topic: topic-1  TopicId: UcKfINbuQbu1Gw1IUVZaxg PartitionCount: 3       ReplicationFactor: 2    Configs:
        Topic: topic-1  Partition: 0    Leader: 2       Replicas: 2,1   Isr: 2,1
        Topic: topic-1  Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: topic-1  Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
```

Add broker. Increase replicaCount in values.yaml to 4 in advance.

```
helm upgrade -n wf wf-kafka-cluster bitnami/kafka -f kafka-sts/values.yaml
```

Check the cluster

```shell
$ kubectl get po -n wf
NAME                           READY   STATUS    RESTARTS        AGE
wf-kafka-cluster-0             1/1     Running   0               15h
wf-kafka-cluster-1             1/1     Running   0               15h
wf-kafka-cluster-2             1/1     Running   0               15h
wf-kafka-cluster-3             1/1     Running   0               26s
wf-kafka-cluster-zookeeper-0   1/1     Running   0               15h
```

Check the ID of the running broker.

```shell
$ zookeeper-shell.sh wf-kafka-cluster-zookeeper:2181 ls /brokers/ids
Connecting to wf-kafka-cluster-zookeeper:2181

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[0, 1, 2, 3]
```

Describe topic topic-1. No change.

```shell
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-1
Topic: topic-1  TopicId: UcKfINbuQbu1Gw1IUVZaxg PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-1  Partition: 0    Leader: 2       Replicas: 2,1   Isr: 2,1
        Topic: topic-1  Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: topic-1  Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
```

Add a partition to topic topic-1. The new broker has been assigned as a follower replica.

```shell
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-1
Topic: topic-1  TopicId: UcKfINbuQbu1Gw1IUVZaxg PartitionCount: 5       ReplicationFactor: 2    Configs: 
        Topic: topic-1  Partition: 0    Leader: 2       Replicas: 2,1   Isr: 2,1
        Topic: topic-1  Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: topic-1  Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
        Topic: topic-1  Partition: 3    Leader: 1       Replicas: 1,0   Isr: 1,0
        Topic: topic-1  Partition: 4    Leader: 2       Replicas: 2,3   Isr: 2,3
```

## Create a topic, add a broker, and then reassign partitions of the topic

Create topic.

```shell
$ kafka-topics.sh --create --zookeeper wf-kafka-cluster-zookeeper:2181 --replication-factor 2 --partitions 3 --topic topic-2
Created topic topic-2.
```

Describe created topic.

```
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-2
Topic: topic-2  TopicId: _MgPr5IFSKK1XjaYlIkhnQ PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-2  Partition: 0    Leader: 2       Replicas: 2,1   Isr: 2,1
        Topic: topic-2  Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: topic-2  Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
```

Add broker. Increase replicaCount in values.yaml to 4 in advance.

```
helm upgrade -n wf wf-kafka-cluster bitnami/kafka -f kafka-sts/values.yaml
```

Check the cluster

```shell
$ kubectl get po -n wf
NAME                           READY   STATUS    RESTARTS        AGE
wf-kafka-cluster-0             1/1     Running   0               20m
wf-kafka-cluster-1             1/1     Running   1 (19m ago)     20m
wf-kafka-cluster-2             1/1     Running   1 (19m ago)     20m
wf-kafka-cluster-3             1/1     Running   0               13s
wf-kafka-cluster-zookeeper-0   1/1     Running   0               20m
```

Check the ID of the running broker.

```shell
$ zookeeper-shell.sh wf-kafka-cluster-zookeeper:2181 ls /brokers/ids
Connecting to wf-kafka-cluster-zookeeper:2181

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[0, 1, 2, 3]
```

Describe topic topic-2. No change.

```shell
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-2
Topic: topic-2  TopicId: _MgPr5IFSKK1XjaYlIkhnQ PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-2  Partition: 0    Leader: 2       Replicas: 2,1   Isr: 2,1
        Topic: topic-2  Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: topic-2  Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
```

Create topics-to-move.json

```shell
cat <<EOF > topics-to-move.json
{
  "topics": [{ "topic": "topic-2" }],
  "version": 1
}
EOF
```

Run kafka-reassign-partitions.sh to create a reassignment plan

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --topics-to-move-json-file topics-to-move.json --broker-list=0,1,2,3 --generate
Warning: --zookeeper is deprecated, and will be removed in a future version of Kafka.
Current partition replica assignment
{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,1],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[0,2],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[1,0],"log_dirs":["any","any"]}]}

Proposed partition reassignment configuration
{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,3],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[3,0],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[0,1],"log_dirs":["any","any"]}]}
```

Create expand-cluster-reassignment.json.

```shell
cat <<EOF > expand-cluster-reassignment.json
{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,3],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[3,0],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[0,1],"log_dirs":["any","any"]}]}
EOF
```

Execute reassignment.

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --reassignment-json-file expand-cluster-reassignment.json --execute
Warning: --zookeeper is deprecated, and will be removed in a future version of Kafka.
Current partition replica assignment

{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,1],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[0,2],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[1,0],"log_dirs":["any","any"]}]}

Save this to use as the --reassignment-json-file option during rollback
Successfully started partition reassignments for topic-2-0,topic-2-1,topic-2-2
```

Verify reasignment.

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --reassignment-json-file expand-cluster-reassignment.json --verify 
Warning: --zookeeper is deprecated, and will be removed in a future version of Kafka.
Warning: because you are using the deprecated --zookeeper option, the results may be incomplete.  Use --bootstrap-server instead for more accurate results.
Status of partition reassignment:
Reassignment of partition topic-2-0 is complete
```

Describe topic.

```
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-2
Topic: topic-2  TopicId: _MgPr5IFSKK1XjaYlIkhnQ PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-2  Partition: 0    Leader: 2       Replicas: 2,3   Isr: 2,3
        Topic: topic-2  Partition: 1    Leader: 3       Replicas: 3,0   Isr: 0,3
        Topic: topic-2  Partition: 2    Leader: 1       Replicas: 0,1   Isr: 1,0
```

## Delete one broker and observe the partition placement

Remove one broker. Decrease replicaCount in values.yaml to 3 in advance.

```shell
helm upgrade -n wf wf-kafka-cluster bitnami/kafka -f kafka-sts/values.yaml
```

Check the cluster

```shell
$ kubectl get po -n wf
NAME                           READY   STATUS    RESTARTS        AGE
wf-kafka-cluster-0             1/1     Running   0               63m
wf-kafka-cluster-1             1/1     Running   1 (63m ago)     63m
wf-kafka-cluster-2             1/1     Running   1 (63m ago)     63m
wf-kafka-cluster-zookeeper-0   1/1     Running   0               63m
```

Check the ID of the running broker.

```shell
$ zookeeper-shell.sh wf-kafka-cluster-zookeeper:2181 ls /brokers/ids
Connecting to wf-kafka-cluster-zookeeper:2181

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[0, 1, 2]
```

Describe topic topic-2. The reader replica has been changed to a running broker. The follower still has the deleted broker.

```shell
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-2
Topic: topic-2  TopicId: _MgPr5IFSKK1XjaYlIkhnQ PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-2  Partition: 0    Leader: 2       Replicas: 2,3   Isr: 2
        Topic: topic-2  Partition: 1    Leader: 0       Replicas: 3,0   Isr: 0
        Topic: topic-2  Partition: 2    Leader: 1       Replicas: 0,1   Isr: 1,0
```

Run kafka-reassign-partitions.sh to create a reassignment plan

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --topics-to-move-json-file topics-to-move.json --broker-list=0,1,2 --generate
Current partition replica assignment
{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,3],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[3,0],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[0,1],"log_dirs":["any","any"]}]}

Proposed partition reassignment configuration
{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,0],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[0,1],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[1,2],"log_dirs":["any","any"]}]}
```

Create expand-cluster-reassignment.json.

```shell
cat <<EOF > expand-cluster-reassignment.json
{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,0],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[0,1],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[1,2],"log_dirs":["any","any"]}]}
EOF
```

Execute reassignment.

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --reassignment-json-file expand-cluster-reassignment.json --execute
Warning: --zookeeper is deprecated, and will be removed in a future version of Kafka.
Current partition replica assignment

{"version":1,"partitions":[{"topic":"topic-2","partition":0,"replicas":[2,3],"log_dirs":["any","any"]},{"topic":"topic-2","partition":1,"replicas":[3,0],"log_dirs":["any","any"]},{"topic":"topic-2","partition":2,"replicas":[0,1],"log_dirs":["any","any"]}]}

Save this to use as the --reassignment-json-file option during rollback
Successfully started partition reassignments for topic-2-0,topic-2-1,topic-2-2
```

Verify reasignment.

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --reassignment-json-file expand-cluster-reassignment.json --verifyWarning: --zookeeper is deprecated, and will be removed in a future version of Kafka.Warning: because you are using the deprecated --zookeeper option, the results may be incomplete.  Use --bootstrap-server instead for more accurate results.
Status of partition reassignment:
Reassignment of partition topic-2-0 is complete.
Reassignment of partition topic-2-1 is complete.
Reassignment of partition topic-2-2 is complete.
Clearing broker-level throttles on brokers 0,1,2
Clearing topic-level throttles on topic topic-2
```

Describe topic. The deleted broker disappeared from the follower replica.

```shell
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-2
Topic: topic-2  TopicId: _MgPr5IFSKK1XjaYlIkhnQ PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-2  Partition: 0    Leader: 2       Replicas: 2,0   Isr: 2,0
        Topic: topic-2  Partition: 1    Leader: 0       Replicas: 0,1   Isr: 0,1
        Topic: topic-2  Partition: 2    Leader: 1       Replicas: 1,2   Isr: 1,2
```

## Reduce the number of brokers after changing to a degraded configuration

Start with 4 brokers

```shell
kubectl get po -n wf
NAME                           READY   STATUS    RESTARTS        AGE
wf-kafka-cluster-0             1/1     Running   0               90m
wf-kafka-cluster-1             1/1     Running   1 (89m ago)     90m
wf-kafka-cluster-2             1/1     Running   1 (89m ago)     90m
wf-kafka-cluster-3             1/1     Running   0               97s
wf-kafka-cluster-zookeeper-0   1/1     Running   0               90m
```

```shell
$ zookeeper-shell.sh wf-kafka-cluster-zookeeper:2181 ls /brokers/ids
Connecting to wf-kafka-cluster-zookeeper:2181

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[0, 1, 2, 3]
```

Create topic.

```shell
$ kafka-topics.sh --create --zookeeper wf-kafka-cluster-zookeeper:2181 --replication-factor 2 --partitions 3 --topic topic-3
Created topic topic-3.
```

Describe topic.

```
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-3
Topic: topic-3  TopicId: 8psRp-LVQheM-5KYg-r4Lg PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-3  Partition: 0    Leader: 0       Replicas: 0,3   Isr: 0,3
        Topic: topic-3  Partition: 1    Leader: 1       Replicas: 1,0   Isr: 1,0
        Topic: topic-3  Partition: 2    Leader: 2       Replicas: 2,1   Isr: 2,1
```

Create topics-to-move.json

```shell
cat <<EOF > topics-to-move.json
{
  "topics": [{ "topic": "topic-3" }],
  "version": 1
}
EOF
```

Create a relocation plan by listing brokers not to be deleted

```shell
kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --topics-to-move-json-file topics-to-move.json --broker-list=0,1,2 --generate
Current partition replica assignment
{"version":1,"partitions":[{"topic":"topic-3","partition":0,"replicas":[0,3],"log_dirs":["any","any"]},{"topic":"topic-3","partition":1,"replicas":[1,0],"log_dirs":["any","any"]},{"topic":"topic-3","partition":2,"replicas":[2,1],"log_dirs":["any","any"]}]}

Proposed partition reassignment configuration
{"version":1,"partitions":[{"topic":"topic-3","partition":0,"replicas":[2,1],"log_dirs":["any","any"]},{"topic":"topic-3","partition":1,"replicas":[0,2],"log_dirs":["any","any"]},{"topic":"topic-3","partition":2,"replicas":[1,0],"log_dirs":["any","any"]}]}
```

Create expand-cluster-reassignment.json.

```shell
cat <<EOF > expand-cluster-reassignment.json
{"version":1,"partitions":[{"topic":"topic-3","partition":0,"replicas":[2,1],"log_dirs":["any","any"]},{"topic":"topic-3","partition":1,"replicas":[0,2],"log_dirs":["any","any"]},{"topic":"topic-3","partition":2,"replicas":[1,0],"log_dirs":["any","any"]}]}
EOF
```

Execute reassignment.

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --reassignment-json-file expand-cluster-reassignment.json --execute
Warning: --zookeeper is deprecated, and will be removed in a future version of Kafka.
Current partition replica assignment

{"version":1,"partitions":[{"topic":"topic-3","partition":0,"replicas":[0,3],"log_dirs":["any","any"]},{"topic":"topic-3","partition":1,"replicas":[1,0],"log_dirs":["any","any"]},{"topic":"topic-3","partition":2,"replicas":[2,1],"log_dirs":["any","any"]}]}

Save this to use as the --reassignment-json-file option during rollback
Successfully started partition reassignments for topic-3-0,topic-3-1,topic-3-2
```

Verify

```shell
$ kafka-reassign-partitions.sh --zookeeper wf-kafka-cluster-zookeeper:2181 --reassignment-json-file expand-cluster-reassignment.json --verify
Warning: --zookeeper is deprecated, and will be removed in a future version of Kafka.
Warning: because you are using the deprecated --zookeeper option, the results may be incomplete.  Use --bootstrap-server instead for more accurate results.
Status of partition reassignment:
Reassignment of partition topic-3-0 is complete.
Reassignment of partition topic-3-1 is complete.
Reassignment of partition topic-3-2 is complete.
Clearing broker-level throttles on brokers 0,1,2,3
Clearing topic-level throttles on topic topic-3
```

Describe

```shell
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-3
Topic: topic-3  TopicId: 8psRp-LVQheM-5KYg-r4Lg PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-3  Partition: 0    Leader: 2       Replicas: 2,1   Isr: 1,2
        Topic: topic-3  Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: topic-3  Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
```

Remove one broker. Decrease replicaCount in values.yaml to 3 in advance.

```shell
helm upgrade -n wf wf-kafka-cluster bitnami/kafka -f kafka-sts/values.yaml
```

Check the cluster

```shell
$ kubectl get po -n wf
NAME                           READY   STATUS    RESTARTS        AGE
wf-kafka-cluster-0             1/1     Running   0               105m
wf-kafka-cluster-1             1/1     Running   1 (104m ago)    105m
wf-kafka-cluster-2             1/1     Running   1 (104m ago)    105m
wf-kafka-cluster-zookeeper-0   1/1     Running   0               105m
```

Check the ID of the running broker.

```shell
$ zookeeper-shell.sh wf-kafka-cluster-zookeeper:2181 ls /brokers/ids
Connecting to wf-kafka-cluster-zookeeper:2181

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[0, 1, 2]
```

Describe topic again.

```
$ kafka-topics.sh --describe --zookeeper wf-kafka-cluster-zookeeper:2181 --topic topic-3
Topic: topic-3  TopicId: 8psRp-LVQheM-5KYg-r4Lg PartitionCount: 3       ReplicationFactor: 2    Configs: 
        Topic: topic-3  Partition: 0    Leader: 2       Replicas: 2,1   Isr: 1,2
        Topic: topic-3  Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: topic-3  Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
```
