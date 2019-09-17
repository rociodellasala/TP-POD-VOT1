#!/bin/sh
cd podtpe
cd server
cd target
cd podtpe-server-1.0-SNAPSHOT
gnome-terminal -e "./run-registry.sh" && gnome-terminal -e "./run-server.sh"

