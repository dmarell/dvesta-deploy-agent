#!/bin/sh

set -e

# Preparations on RPi:
# Append content of ~/<jenkinsuser>/.ssh/id_rsa.pub to TARGET_HOST:/home/pi/.ssh/authorized_keys

while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo "$scriptFilename - deploy java application as an init service"
                        echo " "
                        echo "$scriptFilename [options] serviceName installDir artifactDownloadUrl"
                        echo " "
                        echo "options:"
                        echo "-h, --help                show brief help"
                        echo "-j                        Name of jarfile"
                        echo "-u                        username for download"
                        echo "-p                        password for download"
                        exit 0
                        ;;
                -u)
                        shift
                        if test $# -gt 0; then
                                artifactDownloadUsername=$1
                        else
                                echo "no username specified"
                                exit 1
                        fi
                        shift
                        ;;
                -p)
                        shift
                        if test $# -gt 0; then
                                artifactDownloadPassword=$1
                        else
                                echo "no password specified"
                                exit 1
                        fi
                        shift
                        ;;
                -j)
                        shift
                        if test $# -gt 0; then
                                jarFilename=$1
                        else
                                echo "no jarfilename specified"
                                exit 1
                        fi
                        shift
                        ;;
                *)
                        break
                        ;;
        esac
done

scriptFilename=`basename "$0"`
serviceName=$1
installDir=$2
artifactDownloadUrl=$3
artifactFilename=`basename ${artifactDownloadUrl}`

#echo scriptFilename: $scriptFilename
#echo serviceName: $serviceName
#echo installDir: $installDir
#echo artifactDownloadUrl: $artifactDownloadUrl
#echo artifactFilename: $artifactFilename
#echo jarFilename: $jarFilename
#echo artifactDownloadUsername: $artifactDownloadUsername
#echo artifactDownloadPassword: $artifactDownloadPassword

# Initalizations if first time
#
sudo adduser pi sudo 2> /dev/null
# Make sure user pi can run sudo without entering a password: Append to /etc/sudoers: pi ALL=NOPASSWD: ALL
if sudo -n true 2>/dev/null; then
    echo "Got sudo without password"
else
    if [ $? -ne 0 ] ; then echo 'pi ALL=(ALL:ALL) ALL' >> /etc/sudoers ; fi
fi
sudo usermod -a -G audio pi
mkdir -p ${installDir}

# Download artifact file to installation directory
cd ${installDir}
rm -f *-assemble.tar.gz
if [ -z "$artifactDownloadUsername" ]; then
  curl -O ${artifactDownloadUrl}
else
  curl -O --user ${artifactDownloadUsername}:${artifactDownloadPassword} ${artifactDownloadUrl}
fi

# Stop service if it is running
sudo service ${serviceName} stop 2> /dev/null || true

if [ "$jarFilename" != "" ]; then
  jarFileShort=${serviceName}.jar
  [ ! -f ${jarFileShort} ] || mv -f ${jarFileShort} ${jarFileShort}.old
  # If a jar file with that name already exist, rename it in case the service is still running and locks the file
fi

# Unpack installation file
tar xf ${artifactFilename}

if [ "$jarFilename" != "" ]; then
  if [ -e ${jarFilename} ] && [ "${jarFileShort}" != "${jarFilename}" ]; then
    echo  moving...
    mv ${jarFilename} ${jarFileShort}
  fi
fi

# Install service, if first time
sudo cp ${serviceName} /etc/init.d;
sudo chmod +x /etc/init.d/${serviceName};
sudo update-rc.d ${serviceName} defaults;

# Start service
sudo service ${serviceName} start
