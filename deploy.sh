#!/bin/bash

# 프로젝트 경로 설정
REPOSITORY=/home/ubuntu/app
PROJECT_NAME=spring-basic-roomescape-playground

# 프로젝트 디렉토리로 이동
cd $REPOSITORY/$PROJECT_NAME
echo "> Git Pull"
git pull origin main

echo "> 프로젝트 빌드 시작"
./gradlew clean build

echo "> 빌드 파일 복사"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 PID 확인"
CURRENT_PID=$(lsof -i tcp:8080 | awk 'NR!=1 {print $2}' | sort -u)

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -9 $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 3
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"

# 애플리케이션 실행
nohup java -jar \
    -Dspring.config.location=classpath:/application.yml,/home/ubuntu/app/application-prod.yml \
    -Dspring.profiles.active=prod \
    $JAR_NAME 2>&1 &
