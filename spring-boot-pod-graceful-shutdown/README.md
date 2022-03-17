preStop hook

application.yaml
```yaml
server:
  shutdown: graceful
spring:
  lifecycle:
    timeout-per-shutdown-phase: 60s
```

deployment
```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  replicas: 1
  template:
    spec:
      terminationGracePeriodSeconds: 180
      containers:
        - image: front-api
          lifecycle:
            preStop:
              exec:
                command: ["sh", "-c", "sleep 180"]
```
