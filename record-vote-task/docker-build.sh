#!/bin/bash

_TAG=1.7.1

_IMAGE_NAME=web

echo "remove the local image blackducksoftware/atomic"
#docker rmi $_IMAGE_NAME

echo "building go executable"
docker run --rm -v $(pwd):/usr/src/myapp -w /usr/src/myapp golang:$_TAG bash -c ./build.sh

echo "building new image"
docker build -t $_IMAGE_NAME .
