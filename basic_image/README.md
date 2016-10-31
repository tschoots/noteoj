# description

<br>
this is a Dockerfile to make a image where you can run docker commands against the registry of the docker engine of the host

<br>

start with the following command:

<br>
docker run -it -v /var/run/docker.sock:/var/run/docker.sock ubuntu:latest sh -c "apt-get update ; apt-get install docker.io -y ; bash"

