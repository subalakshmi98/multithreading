#!/bin/bash

mvn clean package || exit 1

if [ $# -ne 1 ]; then
  echo "Usage: ./run.sh [same|multi]"
  exit 1
fi

MODE=$1


# =========================
# SAME JVM
# =========================
if [ "$MODE" = "same" ]; then

  echo "Running SAME JVM..."

  java -cp target/classes org.example.Main

  exit 0
fi


# =========================
# MULTI JVM
# =========================
if [ "$MODE" = "multi" ]; then

  echo "Running MULTI JVM..."

  PORT1=5001
  PORT2=5002

  pkill -f PlayerProcess 2>/dev/null || true
  sleep 1

  echo "Starting P2..."
  java -cp target/classes \
    org.example.PlayerProcess \
    P2 P1 $PORT2 $PORT1 &

  sleep 2

  echo "Starting P1..."
  java -cp target/classes \
    org.example.PlayerProcess \
    P1 P2 $PORT1 $PORT2

  exit 0
fi


echo "Invalid mode"


#Manually
#TERMINAL 1 -> java -cp target/classes org.example.PlayerProcess P2 P1 5011 5012
#TERMINAL 2 -> java -cp target/classes org.example.PlayerProcess P1 P2 5012 5011

#TO KILL PROCESS
#lsof -i :PORT_NO
#KILL -9 PID