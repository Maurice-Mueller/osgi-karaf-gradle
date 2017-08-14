#!/usr/bin/env bash
cd /opt/karaf/bin

state=`./status`
echo $state

while [ "$state" != "Running ..." ]
do
  sleep 1
  echo "waiting for start..."
  state=`./status`
  echo $state
done
