# NBINX Ingress Controller on Minikube 

[Minikube上でNGINX Ingressコントローラーを使用してIngressをセットアップする](https://kubernetes.io/ja/docs/tasks/access-application-cluster/ingress-minikube/)

Start Minikube
```shell
minikube start
```

Enable Ingress Controller
```shell
minikube addons enable ingress
minikube addons enable ingress-dns
```

```shell
$ minikube addons list          
|-----------------------------|----------|--------------|--------------------------------|
|         ADDON NAME          | PROFILE  |    STATUS    |           MAINTAINER           |
|-----------------------------|----------|--------------|--------------------------------|
| ambassador                  | minikube | disabled     | third-party (ambassador)       |
| auto-pause                  | minikube | disabled     | google                         |
| csi-hostpath-driver         | minikube | disabled     | kubernetes                     |
| dashboard                   | minikube | disabled     | kubernetes                     |
| default-storageclass        | minikube | enabled ✅   | kubernetes                     |
| efk                         | minikube | disabled     | third-party (elastic)          |
| freshpod                    | minikube | disabled     | google                         |
| gcp-auth                    | minikube | disabled     | google                         |
| gvisor                      | minikube | disabled     | google                         |
| helm-tiller                 | minikube | disabled     | third-party (helm)             |
| ingress                     | minikube | enabled ✅   | unknown (third-party)          |
| ingress-dns                 | minikube | enabled ✅   | google                         |
| istio                       | minikube | disabled     | third-party (istio)            |
| istio-provisioner           | minikube | disabled     | third-party (istio)            |
| kong                        | minikube | disabled     | third-party (Kong HQ)          |
| kubevirt                    | minikube | disabled     | third-party (kubevirt)         |
| logviewer                   | minikube | disabled     | unknown (third-party)          |
| metallb                     | minikube | disabled     | third-party (metallb)          |
| metrics-server              | minikube | disabled     | kubernetes                     |
| nvidia-driver-installer     | minikube | disabled     | google                         |
| nvidia-gpu-device-plugin    | minikube | disabled     | third-party (nvidia)           |
| olm                         | minikube | disabled     | third-party (operator          |
|                             |          |              | framework)                     |
| pod-security-policy         | minikube | disabled     | unknown (third-party)          |
| portainer                   | minikube | disabled     | portainer.io                   |
| registry                    | minikube | disabled     | google                         |
| registry-aliases            | minikube | disabled     | unknown (third-party)          |
| registry-creds              | minikube | disabled     | third-party (upmc enterprises) |
| storage-provisioner         | minikube | enabled ✅   | google                         |
| storage-provisioner-gluster | minikube | disabled     | unknown (third-party)          |
| volumesnapshots             | minikube | disabled     | kubernetes                     |
|-----------------------------|----------|--------------|--------------------------------|
```

Show pods

```shell
$ kubectl get pod -n ingress-nginx
NAME                                       READY   STATUS      RESTARTS   AGE
ingress-nginx-admission-create-h2lnh       0/1     Completed   0          25m
ingress-nginx-admission-patch-xn7np        0/1     Completed   0          25m
ingress-nginx-controller-cc8496874-mbz9d   1/1     Running     0          25m

$ kubectl get pod -n kube-system -l app=minikube-ingress-dns
NAME                        READY   STATUS    RESTARTS   AGE
kube-ingress-dns-minikube   1/1     Running   0          97s
```

Deploy hello app

```shell
kubectl create deployment web --image=gcr.io/google-samples/hello-app:1.0
```

Show deploy

```shell
$ kubectl get deploy,po
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/web   1/1     1            1           67s

NAME                       READY   STATUS    RESTARTS   AGE
pod/web-746c8679d4-kbwpz   1/1     Running   0          67s
```

Expose deployment web

```shell
kubectl expose deployment web --type=NodePort --port=8080
```

Show services

```shell
$ kubectl get svc web
NAME   TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
web    NodePort   10.106.112.192   <none>        8080:32298/TCP   59s
```

Visit service via NodePort

```shell
$ minikube service web --url
http://192.168.64.14:32298
```

Call API

```shell
$ curl http://192.168.64.14:32298
Hello, world!
Version: 1.0.0
Hostname: web-746c8679d4-kbwpz
```

Setup DNS Resolver of Host OS.

https://minikube.sigs.k8s.io/docs/handbook/addons/ingress-dns/#installation

```shell
sudo mkdir -p /etc/resolver
cat << EOF | sudo tee /etc/resolver/minikube-test
domain minikube.local
nameserver $(minikube ip)
search_order 1
timeout 5
EOF
```

