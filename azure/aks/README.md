# AKS

[クイック スタート:Azure CLI を使用して AKS クラスターをデプロイする - Azure Kubernetes Service](https://docs.microsoft.com/ja-jp/azure/aks/kubernetes-walkthrough)

```
az login
```

Create resource group

```
az group create --name myResourceGroup --location japaneast
```

Chack cluster insights enabled

```
$ az provider show -n Microsoft.OperationsManagement -o table
Namespace                       RegistrationPolicy    RegistrationState
------------------------------  --------------------  -------------------
Microsoft.OperationsManagement  RegistrationRequired  NotRegistered

$ az provider show -n Microsoft.OperationalInsights -o table
Namespace                      RegistrationPolicy    RegistrationState
-----------------------------  --------------------  -------------------
Microsoft.OperationalInsights  RegistrationRequired  Registered
```

Register cluster insights

```
$ az provider register --namespace Microsoft.OperationsManagement
Registering is still on-going. You can monitor using 'az provider show -n Microsoft.OperationsManagement'

$ az provider register --namespace Microsoft.OperationalInsights
```

Create cluster

```
az aks create --resource-group myResourceGroup --name myAKSCluster --node-count 1 --enable-addons monitoring --generate-ssh-keys
```

Config kubectl

```
az aks get-credentials --resource-group myResourceGroup --name myAKSCluster
```

Check cluster connection

```
kubectl get nodes
```

Deploy vote app

```
kubectl apply -f azure-vote.yaml
```

Delete cluster

```
az aks delete --name myAKSCluster --resource-group myResourceGroup
```

Delete resources

```
az group delete --name myResourceGroup --yes --no-wait
```

Delete kubectl context

```
kubectl config delete-context myAKSCluster
```
