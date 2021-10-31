# Kustomize

## Install (Windows)

go get

```
go get sigs.k8s.io/kustomize/kustomize/v3
```

Chocolatey

```
choco install kustomize
```

## Create variants using overlays

## Generate manifest and apply
Generate Yaml

```
kustomize build ./app/overlays/production | kubectl apply -f -
```
