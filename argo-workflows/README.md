Install Argo CLI

https://github.com/argoproj/argo-workflows/releases/latest

Argo Controller


```
kubectl create namespace argo
kubectl apply -n argo -f https://raw.githubusercontent.com/argoproj/argo-workflows/v3.0.2/manifests/install.yaml
```
