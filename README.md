## Dvesta deploy agent

The dvesta deploy agent is a service running on a host. It holds a connection to dvesta gateway through a websocket.
It listen to and execute deploy commands.

## Release notes
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
$ bash deploy.sh -j dvesta-deploy-agent-1.0.2.jar ddagent /home/pi/ddagent \
  http://marell.se/artifactory/libs-release-local/se/marell/dvesta/dvesta-deploy-agent/1.0.2/dvesta-deploy-agent-1.0.2-assemble.tar.gz
```

### Configuration parameters
```
dvesta-gateway.websock-endpoint
dvesta-gateway.username
dvesta-gateway.password
```
