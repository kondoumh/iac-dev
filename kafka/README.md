# Deploy Kafka in local Kubernetes Cluster

https://github.com/bitnami/charts/blob/master/bitnami/kafka/README.md

Install with helm chart

```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install my-release bitnami/kafka
```

```
$ kubectl get po,svc
NAME                         READY   STATUS    RESTARTS   AGE
pod/my-release-kafka-0       1/1     Running   3          37m
pod/my-release-zookeeper-0   1/1     Running   0          37m

NAME                                    TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
service/kubernetes                      ClusterIP   10.96.0.1       <none>        443/TCP                      72d
service/my-release-kafka                ClusterIP   10.105.17.116   <none>        9092/TCP                     37m
service/my-release-kafka-headless       ClusterIP   None            <none>        9092/TCP,9093/TCP            37m
service/my-release-zookeeper            ClusterIP   10.98.145.99    <none>        2181/TCP,2888/TCP,3888/TCP   37m
service/my-release-zookeeper-headless   ClusterIP   None            <none>        2181/TCP,2888/TCP,3888/TCP   37m
```

Kafka can be accessed by consumers via port 9092 on the following DNS name from within your cluster:

    `my-release-kafka.default.svc.cluster.local`

Each Kafka broker can be accessed by producers via port 9092 on the following DNS name(s) from within your cluster:

    `my-release-kafka-0.my-release-kafka-headless.default.svc.cluster.local:9092`

To create a pod that you can use as a Kafka client run the following commands:

```
kubectl run my-release-kafka-client --restart='Never' --image docker.io/bitnami/kafka:2.8.0-debian-10-r0 --namespace default --command -- sleep infinity
kubectl exec --tty -i my-release-kafka-client --namespace default -- bash
```

List topics:

```
$ kafka-topics.sh --list --zookeeper my-release-zookeeper.default.svc.cluster.local:2181
__consumer_offsets
test
```

PRODUCER:

```
kafka-console-producer.sh \
     --broker-list my-release-kafka-0.my-release-kafka-headless.default.svc.cluster.local:9092 \
     --topic test
```

CONSUMER:
```
kafka-console-consumer.sh \
    --bootstrap-server my-release-kafka.default.svc.cluster.local:9092 \
    --topic test \
    --from-beginning
```
