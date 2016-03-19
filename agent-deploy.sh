#!/bin/sh
buildNumber=$1
gwUsername=$2
gwPassword=$3
deployUsername=$4
deployPassword=$5
curl --user ${gwUsername}:${gwPassword} -H "Content-Type: application/json" -X POST -d '{'"command":"deploy ${deployUsername} ${deployPassword} hg31 /home/pi/hg31 http://marell.se/artifactory/hg31/se/marell/hg31/hg31/${buildNumber}/hg31-${buildNumber}-assemble.tar.gz"'}' http://caglabs.se/dvesta-gateway/system/s2/message2?timeout=60000
