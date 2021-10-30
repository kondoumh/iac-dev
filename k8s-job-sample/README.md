k8s-job-sample
====================

Sample Kubernetes Job for uploading file to S3

Create secret for AWS credentials in the Kubernetes cluser.

```
kubectl create secret generic aws-access-key --from-literal=aws_access_key_id=xxxx --from-literal=aws_secret_access_key=xxxx
```

Build and push container image for job pod.

```bash
export AWS_ACCESS_KEY_ID=xxx
export AWS_SECRET_ACCESS_KEY=xxx
$(aws ecr get-login --no-include-email --region ap-northeast-1)
 
export IMAGE_NAME=your/ecr-repo
export IMAGE_TAG=0.1.0
export ECR_REPO=012345678901.dkr.ecr.ap-northeast-1.amazonaws.com
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${ECR_REPO}/${IMAGE_NAME}:${IMAGE_TAG}
docker push ${ECR_REPO}/${IMAGE_NAME}:${IMAGE_TAG}
```

Job manifest example

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: s3cp-job
spec:
  completions: 1
  parallelism: 1
  backoffLimit: 1
  template:
    metadata:
      name: s3cp-job
    spec:
      restartPolicy: Never
      containers:
        - name: pgdump-job
          image: "012345678901.dkr.ecr.ap-northeast-1.amazonaws.com/your/ecr-repo:latest"
          imagePullPolicy: IfNotPresent
          env:
            - name: AWS_ACCESS_KEY_ID
              valueFrom:
                secretKeyRef:
                  name: aws-access-key
                  key: aws_access_key_id
            - name: AWS_SECRET_ACCESS_KEY
              valueFrom:
                secretKeyRef:
                  name: aws-access-key
                  key: aws_secret_access_key
            - name: AWS_DEFAULT_REGION
              value: "ap-northeast-1"
            - name: AWS_BUCKET_NAME
              value: "foo"
            - name: AWS_BUCKET_BACKUP_PATH
              value: "bar"
```

CronJob sample
===============

Apply
```
kubectl apply -f cron-job.yaml
```

Get cronjob

```
$ kubectl get cronjob
NAME    SCHEDULE      SUSPEND   ACTIVE   LAST SCHEDULE   AGE
hello   */1 * * * *   False     0        18s             32s
```

Get pod

```
$ kubectl get po
NAME                   READY   STATUS      RESTARTS   AGE
hello-27259558-zgjcp   0/1     Completed   0          2m27s
hello-27259559-x282k   0/1     Completed   0          87s
hello-27259560-zgckq   0/1     Completed   0          27s
```

Get log

```
$ kubectl logs hello-27259557-24wdz
Sat Oct 30 05:57:07 UTC 2021
Hello from the Kubernetes cluster
```
