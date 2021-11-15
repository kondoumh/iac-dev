# Velero

## Prepare

S3 Bucket

Create aws credentials

```
aws configure
```

Install Velero

```
BUCKET=<bucket name>
REGION=ap-northeast-1
velero install \
   --provider aws \
   --plugins velero/velero-plugin-for-aws:v1.3.0 \
   --bucket $BUCKET \
   --backup-location-config region=$REGION \
   --secret-file ~/.aws/credentials
```

Confirm install

```
$ kubectl get all -n velero
NAME                         READY   STATUS    RESTARTS   AGE
pod/velero-fbf6dfbc8-v6zf8   1/1     Running   0          2m12s

NAME                     READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/velero   1/1     1            1           2m12s

NAME                               DESIRED   CURRENT   READY   AGE
replicaset.apps/velero-fbf6dfbc8   1         1         1       2m12s
```


Uninstalling Velero

```
kubectl delete namespace/velero clusterrolebinding/velero
kubectl delete crds -l component=velero
```
