#!/bin/sh

docker stop monitor
docker rm monitor
docker rmi secretcolossus/monitor:latest

docker build -t secretcolossus/monitor:latest .
docker push secretcolossus/monitor:latest