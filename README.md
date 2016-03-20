## Dvesta deploy agent

The dvesta deploy agent is a service running on a host. It holds a connection to dvesta gateway through a websocket.
It listen to and execute deploy commands.

## Release notes
* Version 1.0.15 - 2016-03-20
  * Wait for deploy command to return and log command result
  * Fixed AppVersion
  * Encourange to change dvesta gateway username and password
* Version 1.0.14 - 2016-03-20
  * deploy.sh bug fix. Logging.
* Version 1.0.13 - 2016-03-20
  * Mode logging.
* Version 1.0.12 - 2016-03-20
  * 104 bug deploy.sh.
* Version 1.0.11 - 2016-03-19
  * Logging. Check errors in deploy script.
* Version 1.0.10 - 2016-03-19
  * New gateway-client.
* Version 1.0.9 - 2016-03-19
  * Added logging.
* Version 1.0.8 - 2016-03-19
  * user s1 -> s2.
* Version 1.0.7 - 2016-03-19
  * Added logging.
* Version 1.0.6 - 2016-03-19
  * More bug fixes.
* Version 1.0.5 - 2016-03-19
  * More bug fixes.
* Version 1.0.4 - 2016-03-18
  * Deploy script bug fixes.
* Version 1.0.3 - 2016-03-18
  * Deploy scripts, installation.
* Version 1.0.2 - 2016-03-17
  * New groupId
* Version 1.0.1 - 2016-03-17
  * New groupId
* Version 1.0.0 - 2016-03-17
  * First release

## Installation
Substitute the version to install below:
```
$ bash deploy.sh -j dvesta-deploy-agent.jar ddagent /home/pi/ddagent \
 http://marell.se/artifactory/libs-release-local/se/marell/dvesta/dvesta-deploy-agent/1.0.15/dvesta-deploy-agent-1.0.15-assemble.tar.gz
```

Change dvesta-gateway username and password:
```
$ vi /etc/init.d/ddagent
...
dvestaGatewayUsername=<yourusername>
dvestaGatewayPassword=<yourpassword>
...
```


### Configuration parameters
```
dvesta-gateway.websock-endpoint
dvesta-gateway.username
dvesta-gateway.password
```
