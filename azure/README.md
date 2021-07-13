# IaC on Azure

## Azure CLI で仮想マシンを作成する

ログイン

```
az login
```

ロケーションを取得

```
$ az account list-locations | grep japaneast
    "id": "/subscriptions/9886644e-0eff-4a2d-9f5f-c3bc768bdf8e/locations/japaneast",
    "name": "japaneast",
          "id": "/subscriptions/9886644e-0eff-4a2d-9f5f-c3bc768bdf8e/locations/japaneast",
          "name": "japaneast",
```

リソースグループを作成

```
$ az group create --name TutorialResources --location japaneast
{
  "id": "/subscriptions/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/resourceGroups/TutorialResources",
  "location": "japaneast",
  "managedBy": null,
  "name": "TutorialResources",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null,
  "type": "Microsoft.Resources/resourceGroups"
}
```

リソースグループのリスト

```
az group list
```

仮想マシンの作成

```
$ az vm create --resource-group TutorialResources \
  --name TutorialVM1 \
  --image UbuntuLTS \
  --generate-ssh-keys \
  --output json \
  --verbose

Use existing SSH public key file: /Users/masa/.ssh/id_rsa.pub
It is recommended to use parameter "--public-ip-sku Standard" to create new VM with Standard public IP. Please note that the default public IP used for VM creation will be changed from Basic to Standard in the future.
{
  "fqdns": "",
  "id": "/subscriptions/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/resourceGroups/TutorialResources/providers/Microsoft.Compute/virtualMachines/TutorialVM1",
  "location": "japaneast",
  "macAddress": "00-0D-3A-78-99-D1",
  "powerState": "VM running",
  "privateIpAddress": "...",
  "publicIpAddress": <PUBLIC_IP_ADDRESS>,
  "resourceGroup": "TutorialResources",
  "zones": ""
}
Command ran in 154.547 seconds (init: 0.120, invoke: 154.427)
```

VM に SSH 接続

```
ssh <PUBLIC_IP_ADDRESS>
```

VM 情報取得

```
az vm show --name TutorialVM1 --resource-group TutorialResources \
      --query 'networkProfile.networkInterfaces[].id' \
      --output tsv
```

NIC オブジェクト ID を環境変数に割り当て

```
NIC_ID=$(az vm show -n TutorialVM1 -g TutorialResources \
  --query 'networkProfile.networkInterfaces[].id' \
  -o tsv)
```

NIC 情報を取得

```
az network nic show --ids $NIC_ID
```

NIC 情報からパブリック IP アドレスとサブネットオブジェクト ID を取得

```
az network nic show --ids $NIC_ID \
  --query '{IP:ipConfigurations[].publicIpAddress.id, Subnet:ipConfigurations[].subnet.id}' \
  -o json
```

read コマンドで TSV を読み込み

```
read -d '' IP_ID SUBNET_ID <<< $(az network nic show \
  --ids $NIC_ID \
  --query '[ipConfigurations[].publicIpAddress.id, ipConfigurations[].subnet.id]' \
  -o tsv)
```

パブリック IP アドレスをルックアップ

```
VM1_IP_ADDR=$(az network public-ip show --ids $IP_ID \
  --query ipAddress \
  -o tsv)
```

IP アドレスを確認
```
echo $VM1_IP_ADDR
```

既存のサブネットでの新規 VM 作成

```
VM2_IP_ADDR=$(az vm create -g TutorialResources \
  -n TutorialVM2 \
  --image UbuntuLTS \
  --generate-ssh-keys \
  --subnet $SUBNET_ID \
  --query publicIpAddress \
  -o tsv)
```

SSH 接続

```
ssh $VM2_IP_ADDR
```

クリーンアップ

```
az group delete --name TutorialResources --no-wait
```

クリーンアップをウォッチする

```
az group wait --name TutorialResources --deleted
```
