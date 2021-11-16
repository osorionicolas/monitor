#!/bin/sh

docker stop monitor
docker rm monitor

docker pull secretcolossus/monitor:latest

docker run -it -d --restart=always -p 8060:8080 --name monitor -v ./external/urls.json:/app/resources/urls.json -v ./external/application.properties:/app/resources/application.properties secretcolossus/monitor:latest