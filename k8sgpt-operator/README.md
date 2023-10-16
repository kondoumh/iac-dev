# k8sgpt-operator

[GitHub - k8sgpt-ai/k8sgpt-operator: Automatic SRE Superpowers within your Kubernetes cluster](https://github.com/k8sgpt-ai/k8sgpt-operator)


```shell
helm repo add k8sgpt https://charts.k8sgpt.ai/
helm repo update
helm install release k8sgpt/k8sgpt-operator -n k8sgpt-operator-system --create-namespace
```

```shell
$ kubectl -n k8sgpt-operator-system get po -w
NAME                                                          READY   STATUS              RESTARTS   AGE
release-k8sgpt-operator-controller-manager-5967d7b996-5khd5   0/2     ContainerCreating   0          39s
release-k8sgpt-operator-controller-manager-5967d7b996-5khd5   1/2     Running             0          72s
release-k8sgpt-operator-controller-manager-5967d7b996-5khd5   2/2     Running             0          81s
```

```shell
kubectl -n k8sgpt-operator-system get po,svc
NAME                                                              READY   STATUS    RESTARTS   AGE
pod/release-k8sgpt-operator-controller-manager-5967d7b996-5khd5   2/2     Running   0          7m3s

NAME                                                              TYPE        CLUSTER-IP        EXTERNAL-IP   PORT(S)    AGE
service/release-k8sgpt-opera-controller-manager-metrics-service   ClusterIP   192.168.194.157   <none>        8443/TCP   7m3s
```

```shell
kubectl create secret generic k8sgpt-sample-secret --from-literal=openai-api-key=$OPENAI_TOKEN -n k8sgpt-operator-system
```
