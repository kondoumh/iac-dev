```
cd demo
eval $(minikube docker-env)
docker build -t demo .
kubectl apply -f deployment.yaml
```
