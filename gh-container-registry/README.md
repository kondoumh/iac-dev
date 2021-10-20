# GitHub container registory

Make Personal access token that can read / write packages and save to file ghcr.txt.

Run docker login.

```
cat ghcr.txt | docker login ghcr.io -u kondoumh --password-stdin
```

Build container image and push to container registry.

```
pushd ../local-minikube-docker
docker build -t nodejs-server .
docker tag nodejs-server ghcr.io/kondoumh/gh-container-registry/nodejs-server:latest
docker push ghcr.io/kondoumh/gh-container-registry/nodejs-server:latest
popd
```
