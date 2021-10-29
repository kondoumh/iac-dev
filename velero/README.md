# Velero

## Prepare

S3 Bucket

Create aws credentials

```
aws configure
```

Install Velero

```
velero install \
   --provider aws \
   --use-restic \
   --plugins velero/velero-plugin-for-aws:v1.3.0 \
   --bucket kubedemo \
   --secret-file ~./aws/credentials \
   --backup-location-config region=
```
