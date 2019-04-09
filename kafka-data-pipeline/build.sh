#!/usr/bin/env bash
./gradlew clean assemble
docker build -t data-pipeline .
docker tag data-pipeline:latest 111909622691.dkr.ecr.ap-southeast-1.amazonaws.com/data-pipeline:3.4.2
docker push 111909622691.dkr.ecr.ap-southeast-1.amazonaws.com/data-pipeline:3.4.2
