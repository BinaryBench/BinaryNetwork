#!/bin/sh
BINDIR=$(dirname "$(readlink -fn "$0")")
cd "$BINDIR"
while true; do java -Xmx1024M -jar  spigot-1.8.8.jar -o true; done
