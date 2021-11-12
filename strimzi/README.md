# Strimzi

[Strimzi - Apache Kafka on Kubernetes](https://strimzi.io/)

## Install with Strimzi

[Quickstarts](https://strimzi.io/quickstarts/)

Create kafka namespace and apply strimzi installation file
```
kubectl create namespace kafka
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka
```

List crds
```
$ kubectl -n kafka get crd
NAME                                         CREATED AT
eniconfigs.crd.k8s.amazonaws.com             2021-11-09T00:36:50Z
kafkabridges.kafka.strimzi.io                2021-11-09T01:31:26Z
kafkaconnectors.kafka.strimzi.io             2021-11-09T01:31:26Z
kafkaconnects.kafka.strimzi.io               2021-11-09T01:31:26Z
kafkamirrormaker2s.kafka.strimzi.io          2021-11-09T01:31:25Z
kafkamirrormakers.kafka.strimzi.io           2021-11-09T01:31:26Z
kafkarebalances.kafka.strimzi.io             2021-11-09T01:31:24Z
kafkas.kafka.strimzi.io                      2021-11-09T01:31:24Z
kafkatopics.kafka.strimzi.io                 2021-11-09T01:31:25Z
kafkausers.kafka.strimzi.io                  2021-11-09T01:31:24Z
securitygrouppolicies.vpcresources.k8s.aws   2021-11-09T00:36:54Z
```

get operator

```
$ kubectl -n kafka get all      
NAME                                         READY   STATUS    RESTARTS   AGE
pod/strimzi-cluster-operator-85bb4c6-j8nnh   1/1     Running   0          35s

NAME                                       READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/strimzi-cluster-operator   1/1     1            1           37s

NAME                                               DESIRED   CURRENT   READY   AGE
replicaset.apps/strimzi-cluster-operator-85bb4c6   1         1         1       36s
```

Provision the kafka cluster
```
kubectl apply -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n kafka
```

Wait for kafka cluster is ready
```
$ kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka
kafka.kafka.strimzi.io/my-cluster condition met
```

Manifest applied
```yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: Kafka
metadata:
  name: my-cluster
spec:
  kafka:
    version: 2.8.0
    replicas: 1
    listeners:
      - name: plain
        port: 9092
        type: internal
        tls: false
      - name: tls
        port: 9093
        type: internal
        tls: true
    config:
      offsets.topic.replication.factor: 1
      transaction.state.log.replication.factor: 1
      transaction.state.log.min.isr: 1
      log.message.format.version: "2.8"
      inter.broker.protocol.version: "2.8"
    storage:
      type: jbod
      volumes:
      - id: 0
        type: persistent-claim
        size: 100Gi
        deleteClaim: false
  zookeeper:
    replicas: 1
    storage:
      type: persistent-claim
      size: 100Gi
      deleteClaim: false
  entityOperator:
    topicOperator: {}
    userOperator: {}
```

List pod, service, deployment
```
$ kubectl -n kafka get all
NAME                                              READY   STATUS    RESTARTS   AGE
pod/my-cluster-entity-operator-6b495ccbc5-c8ps5   3/3     Running   0          3m19s
pod/my-cluster-kafka-0                            1/1     Running   0          4m
pod/my-cluster-zookeeper-0                        1/1     Running   0          4m50s
pod/strimzi-cluster-operator-85bb4c6-j8nnh        1/1     Running   0          11m

NAME                                  TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                               AGE
service/my-cluster-kafka-bootstrap    ClusterIP   172.20.221.251   <none>        9091/TCP,9092/TCP,9093/TCP            4m
service/my-cluster-kafka-brokers      ClusterIP   None             <none>        9090/TCP,9091/TCP,9092/TCP,9093/TCP   4m
service/my-cluster-zookeeper-client   ClusterIP   172.20.194.201   <none>        2181/TCP                              4m51s
service/my-cluster-zookeeper-nodes    ClusterIP   None             <none>        2181/TCP,2888/TCP,3888/TCP            4m51s

NAME                                         READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/my-cluster-entity-operator   1/1     1            1           3m19s
deployment.apps/strimzi-cluster-operator     1/1     1            1           11m

NAME                                                    DESIRED   CURRENT   READY   AGE
replicaset.apps/my-cluster-entity-operator-6b495ccbc5   1         1         1       3m19s
replicaset.apps/strimzi-cluster-operator-85bb4c6        1         1         1       11m

NAME                                    READY   AGE
statefulset.apps/my-cluster-kafka       1/1     4m
statefulset.apps/my-cluster-zookeeper   1/1     4m50s
```

Undeploy kafka cluster

```
kubectl delete -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n kafka
```

Undeploy Strimzi

```
kubectl delete -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka
kubectl delete ns kafka
```


## Debezium

```
curl https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.7.1.Final/debezium-connector-postgres-1.7.1.Final-plugin.tar.gz | tar xvz
docker build . -t my-connect-debezium
```
