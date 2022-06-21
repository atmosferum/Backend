#!/bin/bash

APP_VERSION=0.0.1-SNAPSHOT
APP_NAME=whattime

cd ..

echo "Building JAR files"
mvn clean package -DskipTests

echo "Unpacking JAR"
cd target || exit 1
java -jar -Djarmode=layertools "${APP_NAME}"-"${APP_VERSION}".jar extract
cd ..

echo "Building Docker image"
docker build -f delivery/docker/Dockerfile \
    --build-arg JAR_FOLDER=/target \
    -t "${APP_NAME}":latest \
    -t "${APP_NAME}":"${APP_VERSION}" .