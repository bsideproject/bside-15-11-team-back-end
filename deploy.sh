#!/bin/bash

REPOSITORY=~/app/backend

echo "> 실행중인 애플리케이션 pid 확인"
CURRENT_PID=$(lsof -i tcp:8080 | awk 'NR!=1 {print$2}')

echo "> 실행중인 애플리케이션 pid: $CURRENT_PID"
if [ -z $CURRENT_PID ]; then
  echo "> 실행중인 애플리케이션 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 3
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ |grep jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"

nohup java -jar -Dspring.profiles.active=release $REPOSITORY/$JAR_NAME > nohup.out 2> nohup.err < /dev/null &