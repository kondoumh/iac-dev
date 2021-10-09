# local-minikube-docker
Example project on how to use local docker images on minikube

https://github.com/bbachi/local-minikube-docker


Build the docker image

```
docker build -t nodejs-server .
```

Build image with the Minikube's Docker daemon

```
eval $(minikube docker-env)
docker build -t nodejs-server .
```

Deploy to minikube

```
kubectl apply -f manifest.yml
```

Call API

```
kubectl run -it --rm=true busybox --image=yauritux/busybox-curl --restart=Never
# curl http://nodejs-service:3000/hello?name=foo
```
