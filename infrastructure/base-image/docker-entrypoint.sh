#!/bin/bash

# Start the first process
cd /beat
filebeat -e &
status=$?
if [ $status -ne 0 ]; then
  echo "Failed to start filebeat: $status"
  exit $status
fi

# Start the second process
/usr/share/run_app.sh
status=$?
echo "Application stopped, wait 60 seconds to ship some logs"
sleep 60
if [ $status -ne 0 ]; then
  echo "Failed to start application: $status"
  echo "Failed to start application" > /logs/container.log
  exit $status
fi

# Naive check runs checks once a minute to see if either of the processes exited.
# This illustrates part of the heavy lifting you need to do if you want to run
# more than one service in a container. The container will exit with an error
# if it detects that either of the processes has exited.
# Otherwise it will loop forever, waking up every 60 seconds

#while /bin/true; do
#  PROCESS_1_STATUS=$(ps aux |grep -q filebeat |grep -v grep)
#  PROCESS_2_STATUS=$(ps aux |grep -q java | grep -v grep)
#  # If the greps above find anything, they will exit with 0 status
#  # If they are not both 0, then something is wrong
#  if [ $PROCESS_1_STATUS -ne 0 -o $PROCESS_2_STATUS -ne 0 ]; then
#    echo "One of the processes has already exited."
#    exit -1
#  fi
#  sleep 15
#done
