# Velero

## Prepare

Run minio container
```
```

Install Velero

```
velero install \
   --provider aws \
   --use-restic \
   --plugins velero/velero-plugin-for-aws:v1.0.0 \
   --bucket kubedemo \
   --secret-file ./velero-demo/minio.credentials \
   --backup-location-config region=minio,s3ForcePathStyle=true,s3Url=http://<ip>:9000
```
