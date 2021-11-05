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
NAME                                  CREATED AT
kafkabridges.kafka.strimzi.io         2021-09-28T07:55:56Z
kafkaconnectors.kafka.strimzi.io      2021-09-28T07:55:56Z
kafkaconnects.kafka.strimzi.io        2021-09-28T07:55:56Z
kafkamirrormaker2s.kafka.strimzi.io   2021-09-28T07:55:55Z
kafkamirrormakers.kafka.strimzi.io    2021-09-28T07:55:56Z
kafkarebalances.kafka.strimzi.io      2021-09-28T07:55:55Z
kafkas.kafka.strimzi.io               2021-09-28T07:55:55Z
kafkatopics.kafka.strimzi.io          2021-09-28T07:55:56Z
kafkausers.kafka.strimzi.io           2021-09-28T07:55:55Z
```

get operator

```
$ kubectl -n kafka get po,deploy
NAME                                            READY   STATUS    RESTARTS   AGE
pod/strimzi-cluster-operator-7695cb5f7f-nv8jf   1/1     Running   0          87s

NAME                                       READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/strimzi-cluster-operator   1/1     1            1           88s
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
$ kubectl -n kafka get po,svc,deploy
NAME                                              READY   STATUS    RESTARTS   AGE
pod/my-cluster-entity-operator-69d9c98979-2nwtw   3/3     Running   0          105s
pod/my-cluster-kafka-0                            1/1     Running   0          2m11s
pod/my-cluster-zookeeper-0                        1/1     Running   0          2m35s
pod/strimzi-cluster-operator-7695cb5f7f-nv8jf     1/1     Running   0          5m4s

NAME                                  TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                               AGE
service/my-cluster-kafka-bootstrap    ClusterIP   10.109.99.14     <none>        9091/TCP,9092/TCP,9093/TCP            2m11s
service/my-cluster-kafka-brokers      ClusterIP   None             <none>        9090/TCP,9091/TCP,9092/TCP,9093/TCP   2m11s
service/my-cluster-zookeeper-client   ClusterIP   10.111.225.156   <none>        2181/TCP                              2m36s
service/my-cluster-zookeeper-nodes    ClusterIP   None             <none>        2181/TCP,2888/TCP,3888/TCP            2m36s

NAME                                         READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/my-cluster-entity-operator   1/1     1            1           105s
deployment.apps/strimzi-cluster-operator     1/1     1            1           5m5s
```

Send and receive messages
```
kubectl -n kafka run kafka-producer -ti --image=quay.io/strimzi/kafka:0.25.0-kafka-2.8.0 --rm=true --restart=Never -- bin/kafka-console-producer.sh --broker-list my-cluster-kafka-bootstrap:9092 --topic my-topic

kubectl -n kafka run kafka-consumer -ti --image=quay.io/strimzi/kafka:0.25.0-kafka-2.8.0 --rm=true --restart=Never -- bin/kafka-console-consumer.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 --topic my-topic --from-beginning
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
