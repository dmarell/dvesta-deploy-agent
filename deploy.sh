#!/bin/sh

#
# Deploy artifact on this host.
#
# Preparations on RPi:
# 1) Append content of ~/<jenkinsuser>/.ssh/id_rsa.pub to TARGET_HOST:/home/pi/.ssh/authorized_keys
# 2) Make sure user pi can run sudo: sudo adduser pi sudo
# 3) Make sure user pi can run sudo without entering a password: Append to /etc/sudoers: pi ALL=NOPASSWD: ALL
#

ARTIFACT_DOWNLOAD_URL=$1
ARTIFACT_DOWNLOAD_USER=$2
ARTIFACT_DOWNLOAD_PASSWORD=$3
ARTIFACT_FILENAME=$4
INSTALL_DIR=$5
JARFILE=$6
SERVICE_NAME=$7

# Initalizations if first time
sudo usermod -a -G audio pi
mkdir -p ${INSTALL_DIR}

cd ${INSTALL_DIR}

# Download artifact file to installation directory
curl -O --user ${ARTIFACT_DOWNLOAD_USER}:${ARTIFACT_DOWNLOAD_PASSWORD} ${ARTIFACT_DOWNLOAD_URL}

# Stop service if it is running
sudo service ${SERVICE_NAME} stop 2> /dev/null || true

# If a jar file with that name already exist, rename it in case the service is still running and locks the file
[ ! -f ${JARFILE} ] || mv -f ${JARFILE} ${JARFILE}.old

# Unpack installation file
tar xf ${ARTIFACT_FILENAME}

# Install service, if first time
sudo cp ${SERVICE_NAME} /etc/init.d;
sudo chmod +x /etc/init.d/${SERVICE_NAME};
sudo update-rc.d ${SERVICE_NAME} defaults;

# Start service
sudo service ${SERVICE_NAME} start
