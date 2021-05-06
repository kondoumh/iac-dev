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

Submit an example workflow

```
argo submit -n argo --watch hello-world.yaml

argo list -n argo

argo get -n argo @latest

argo logs -n argo @latest
```
