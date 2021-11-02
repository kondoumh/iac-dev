# wasmCloud

## Installation

wash

Homebrew

```
brew tap wasmcloud/wasmcloud
brew install wash
```

Cargo

In Windows, You will need to install Microsoft C++ build tools previously.

```
cargo install wash-cli
```

Installing NATS and the wasmCloud host runtime (with docker)

https://raw.githubusercontent.com/wasmCloud/examples/main/docker/docker-compose.yml


```
docker-compose up
```

wasmCloud dashboard

http://localhost:4000


See a list of running hosts

```
$ wash ctl get hosts
⠉⠙  Retrieving Hosts ...
  Host ID                                                    Uptime (seconds)
  NAZXAFBAKWP7O5Z56YMXKY6JCWVMW7QWESQNPWCHJTPDX6IOD42D7YFY   167
```

View hosts inventory

```
$ wash ctl get inventory NAZXAFBAKWP7O5Z56YMXKY6JCWVMW7QWESQNPWCHJTPDX6IOD42D7YFY
⡋⠁  Retrieving inventory for host NAZXAFBAKWP7O5Z56YMXKY6JCWVMW7QWESQNPWCHJTPDX6IOD42D7YFY ...
  Host Inventory (NAZXAFBAKWP7O5Z56YMXKY6JCWVMW7QWESQNPWCHJTPDX6IOD42D7YFY)  

  hostcore.arch                         x86_64
  hostcore.os                           linux
  hostcore.osfamily                     unix

  No actors found

  No providers found
```

Run actor `wasmcloud.azurecr.io/echo:0.3.2` with Web UI

Inspect actor

```
$ wash claims inspect wasmcloud.azurecr.io/echo:0.3.2
 
                               echo - Module
  Account       ACOJJN6WUP4ODD75XEBKKTCCUJJCY5ZKQ56XVKYK4BEJWGVAOOQHZMCW
  Module        MBCFOPM6JW2APJLXJD3Z5O4CN7CPYJ2B4FTKLJUR5YR5MITIU7HD3WD5
  Expires                                                          never
  Can Be Used                                                immediately
  Version                                                      0.3.2 (4)
  Call Alias                                                   (Not set)
                               Capabilities
  HTTP Server
                                   Tags
  None
```

Start HTTP server capability `wasmcloud.azurecr.io/httpserver:0.14.4` with Web UI

Get inventory again.


```
wash ctl get inventory NAZXAFBAKWP7O5Z56YMXKY6JCWVMW7QWESQNPWCHJTPDX6IOD42D7YFY
⡃⠀  Retrieving inventory for host NAZXAFBAKWP7O5Z56YMXKY6JCWVMW7QWESQNPWCHJTPDX6IOD42D7YFY ...
                                  Host Inventory(NAZXAFBAKWP7O5Z56YMXKY6JCWVMW7QWESQNPWCHJTPDX6IOD42D7YFY)

  hostcore.osfamily                                                              unix
  hostcore.os                                                                    linux
  hostcore.arch                                                                  x86_64

  Actor ID                                                    Name               Image Reference
  MBCFOPM6JW2APJLXJD3Z5O4CN7CPYJ2B4FTKLJUR5YR5MITIU7HD3WD5    N/A                wasmcloud.azurecr.io/echo:0.3.2

  Provider ID                                                 Name               Link Name          Image Reference
  VAG3QITQQ2ODAOWB5TTQSDJ53XK3SHBEIFNK4AYJ5RKAX2UNSCAPHA5M    N/A                default            wasmcloud.azurecr.io/httpserver:0.14.4
```

Link actors and capalility providers with Web UI

| Item        | Value                 |
|:------------|:----------------------|
| Actor       | echo (MBCFO..)        |
| Provider    | httpserver (VAG3Q...) |
| Link Name   | default               |
| Contract ID | wasmcloud:httpserver  |
| Values      | address=0.0.0.0:8080  |

Interact with actor

```
$ curl localhost:8080/echo | jq .
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    59  100    59    0     0   3470      0 --:--:-- --:--:-- --:--:--  3470
{
  "body": [],
  "method": "GET",
  "path": "/echo",
  "query_string": ""
}
```

Find Cluster Seed from log and export it as an environment variable

```
$ export WASMCLOUD_CLUSTER_SEED=SCACWMRQTVXVDOMMHE6LKXPPNMP5DWJTWHJY3JTZVKQREKXWQQUPW5G35I
```

wash call

```
$ wash call MBCFOPM6JW2APJLXJD3Z5O4CN7CPYJ2B4FTKLJUR5YR5MITIU7HD3WD5 HttpServer.HandleRequest '{"method": "GET", "path": "/echo", "body": "", "queryString":"","header":{}}'

Call response (raw): ��body�;{"body":[],"method":"GET","path":"/echo","query_string":""}�header��statusCode��
```
