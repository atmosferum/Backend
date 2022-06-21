#!/bin/bash

sh ./build-image-layered.sh

docker run -e SPRING_PROFILES_ACTIVE=h2 whattime:latest