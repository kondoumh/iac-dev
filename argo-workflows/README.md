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

Submit coinflip example

```
argo submit -n argo --watch coinflip.yaml
```

Submit retry backoff

```
argo submit -n argo --watch retry-backoff.yaml
```

Submit coinflip recursive example

```
$ argo submit -n argo --watch coinflip-recursive.yaml

STEP                          TEMPLATE   PODNAME                              DURATION  MESSAGE
 ✔ coinflip-recursive-76sdz   coinflip
 ├───✔ flip-coin              flip-coin  coinflip-recursive-76sdz-1210589150  6s
 └─┬─○ heads                  heads
   └─✔ tails                  coinflip
     ├───✔ flip-coin          flip-coin  coinflip-recursive-76sdz-2126060946  5s
     └─┬─○ heads              heads                                                     when 'tails == heads' evaluated false
       └─✔ tails              coinflip
         ├───✔ flip-coin      flip-coin  coinflip-recursive-76sdz-957147006   6s
         └─┬─○ heads          heads                                                     when 'tails == heads' evaluated false
           └─✔ tails          coinflip
             ├───✔ flip-coin  flip-coin  coinflip-recursive-76sdz-3152068274  5s
             └─┬─✔ heads      heads      coinflip-recursive-76sdz-3040385095  5s
               └─○ tails      coinflip                                                  when 'heads == tails' evaluated false
```

Submit exit handler example

```
$ argo submit -n argo --watch exit-handlers.yaml

STEP                           TEMPLATE          PODNAME                         DURATION  MESSAGE
 ✖ exit-handlers-2xxzs         intentional-fail  exit-handlers-2xxzs             7s        Error (exit code 1)

 ✔ exit-handlers-2xxzs.onExit  exit-handler
 └─┬─○ celebrate               celebrate                                                   when 'Failed == Succeeded' evaluated false
   ├─✔ cry                     cry               exit-handlers-2xxzs-914918728   11s
   └─✔ notify                  send-email        exit-handlers-2xxzs-3516606585  9s
```

Submit timeout example

```
argo submit -n argo --watch timeout.yaml
```

Submit volume pvc example

```
argo submit -n argo --watch volumes-pvc.yaml
```

Submit suspending example

```
argo submit -n argo --watch suspend-template.yaml
```

To resume suspended workflow

```
argo resume -n argo suspend-template-5bmk5
```

Submit daemon containers example

```
argo submit -n argo --watch daemon-example.yaml
```

Submit sidecar example

```
argo submit -n argo --watch sidecar-nginx.yaml
```

Submit hardwired artifact example

```
argo submit -n argo --watch input-artifact-git.yaml
```

Submit kubernetes resource example

```
argo submit -n argo --watch k8s-jobs.yaml
```
