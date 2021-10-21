# GitHub container registory

## Build and push image
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

## Pull image in Kubernetes cluster

Create secret and confirm content
```
$ kubectl create secret docker-registry regcred \
  --docker-server=https://ghcr.io \
  --docker-username=kondoumh \
  --docker-password=<github_personal_access_token>
secret/regcred created

$ kubectl get secret regcred -oyaml
apiVersion: v1
data:
  .dockerconfigjson:  <base64_encoded_token>
kind: Secret
metadata:
  creationTimestamp: "2021-10-21T00:24:40Z"
  name: regcred
  namespace: default
```

Apply deployment

```
$ kubectl apply -f manifest.yaml
deployment.apps/nodejs-api created
service/nodejs-service created

$ kubectl get po
NAME                          READY   STATUS    RESTARTS   AGE
nodejs-api-86f69c99bd-697k6   1/1     Running   0          49s
nodejs-api-86f69c99bd-w2kcg   1/1     Running   0          49s

$ kubectl get deploy nodejs-api -o wide
NAME         READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS      IMAGES                                                        SELECTOR
nodejs-api   2/2     2            2           82s   nodejs-server   ghcr.io/kondoumh/gh-container-registry/nodejs-server:latest   app=nodejs-api
```
