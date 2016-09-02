#!/usr/bin/env bash
cd ./Libraries

SERVER_JAR=spigot-1.8.8.jar
IGNORE_FILES=( $SERVER_JAR BungeeCord.jar CreateServer.sh)

for server_name in Hub Game
do
    server_root="../Servers/$server_name"
    server_plugins=$server_root/plugins

    mkdir $server_root
    mkdir $server_plugins

    cp $SERVER_JAR $server_root
    for file in $(ls)
    do
        if [[ " ${IGNORE_FILES[*]} " != *" $file "* ]]; then
            cp $file $server_plugins
        fi
    done

    echo '#!/bin/sh
BINDIR=$(dirname "$(readlink -fn "$0")")
cd "$BINDIR"
java -Xmx1024M -jar ' $SERVER_JAR '-o true' > $server_root/linuxrun.sh

    echo '#!/bin/bash
cd "$( dirname "$0" )"
java -Xmx1024M -jar' $SERVER_JAR '-o true' > $server_root/macrun.sh

    echo 'java -Xmx1024M -jar' $SERVER_JAR '-o true
PAUSE' > $server_root/windowsrun.bat

    echo 'eula=true' > $server_root/eula.txt


done