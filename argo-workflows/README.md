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
