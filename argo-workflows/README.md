Install Argo CLI

https://github.com/argoproj/argo-workflows/releases/latest

Argo Controller


Argo Install Manifests

https://github.com/argoproj/argo-workflows/tree/master/manifests

```
kubectl create namespace argo
kubectl apply -n argo -f https://raw.githubusercontent.com/argoproj/argo-workflows/stable/manifests/quick-start-postgres.yaml
```

open port-forward (running locally with Docker for Desktop etc)

```
kubectl -n argo port-forward deployment/argo-server 2746:2746
```

Submit hello-world workflow

```
argo submit -n argo --watch hello-world.yaml
argo list -n argo
argo get -n argo @latest
argo logs -n argo @latest
```

Submit workflow with parameter

```
argo submit -n argo --watch arguments-parameters.yaml -p message="goodbye world"
```

Submit steps workflow

```
argo submit -n argo --watch steps.yaml
```

```
STEP            TEMPLATE           PODNAME                 DURATION  MESSAGE
 ✔ steps-lmp8k  hello-hello-hello
 ├───✔ hello1   whalesay           steps-lmp8k-1250056042  9s
 └─┬─✔ hello2a  whalesay           steps-lmp8k-4119689043  14s
   └─✔ hello2b  whalesay           steps-lmp8k-4136466662  11s
```

Submit dag workflow

```
argo submit -n argo --watch dag-diamond.yaml
```

Submit artifacts workflow

before submit setup artifact repository

```
helm repo add minio https://helm.min.io/
helm install argo-artifacts minio/minio --set service.type=LoadBalancer --set fullnameOverride=argo-artifacts
```

```
$ kubectl get svc argo-artifacts -n argo
NAME             TYPE           CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
argo-artifacts   LoadBalancer   10.99.59.234   localhost     9000:32040/TCP   74s
```

```
argo submit -n argo --watch artifact-passing.yaml
```


Submit secrets workflow

create the secret previously.

```
kubectl -n argo create secret generic my-secret --from-literal=mypassword=S00perS3cretPa55word
```

```
argo submit -n argo --watch secrets.yaml
```

Submit scripts workflow

```
argo submit -n argo --watch script-bash.yaml
```

Submit output param workflow

```
argo submit -n argo --watch output-parameter.yaml
```


Submit loops workflow

```
argo submit -n argo --watch loops.yaml
```

Submit loops with map

```
argo submit -n argo --watch loops-maps.yaml
```

Submit loops as param

```
argo submit -n argo --watch loops-param-argument.yaml
```

Sumbit loops result

```
argo submit -n argo --watch loops-param-result.yaml
```

```
argo submit -n argo --watch coinflip.yaml
```
