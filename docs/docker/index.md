# Docker

Docker is a tool used to containerize operating systems. It facilitates software development by mitigating the need for virtual machines (in most cases).

## Table of Contents

- [Getting started](#getting-started)
- [Docker image vs. Docker container vs. Docker volume](#docker-image-vs-docker-container-vs-docker-volume)
- [Dockerfile](#dockerfile)
- [Creating images, containers, and volumes](#creating-images-containers-and-volumes)
- [Interacting with containers](#interacting-with-containers)
- [Stopping containers](#stopping-containers)
- [Informational commands](#informational-commands)
- [Removing images, containers, and volumes](#removing-images-containers-and-volumes)
- [Docker Compose](#docker-compose)

## Getting Started

Run the following commands in Terminal or Windows PowerShell to quickly get an Ubuntu container up:

```
docker pull ubuntu
docker run -it --name ubuntu ubuntu /bin/bash
```

- `docker pull ubuntu` pulls the latest version of the [official Ubuntu image](https://hub.docker.com/_/ubuntu) on Docker Hub
- From the `docker run` command, `--name ubuntu` names the container "ubuntu" and builds the container using the official `ubuntu` image that you just pulled
- `-it` keeps the terminal window open, and `/bin/bash` specifies that the bash terminal shall be used

Congratulations! You now have terminal access to a bare-bones Ubuntu. You can try experimenting with some [terminal commands](../terminal-commands#the-36-most-important-terminal-commands-plus-symbols-and-compression). When you're done, you can exit the Ubuntu container by running `exit`.

## Docker image vs. Docker container vs. Docker volume

- an image is an immutable file that contains files needed to run a container
- a container is a runtime environment that runs on top of an image
    - unlike virtual machines, they virtualize at the application layer and share the same underlying "OS" (container engine)
- a volume is a file system (a directory) mounted to a container
    - this allows data to be backed up and saved, regardless of container status
    - this also allows data to be shared between containers

## Dockerfile

- a script of instructions that tells Docker how to build an image
- `FROM` defines the base of the image, usually a parent image
    - e.g. `FROM [custom_repository/]ubuntu[:latest]`
- (optional) `WORKDIR` sets the working directory
    - e.g. `WORKDIR /code`
- (optional) `COPY` copies a file from the current host directory to the image
    - e.g. `COPY file.txt .`
- (optional) `EXPOSE` exposes a port to the host OS
    - e.g. `EXPOSE 80/tcp`
- `RUN` specifies commands to run while building an image in the layer on top of it
    - e.g. `RUN apt update && apt install cmake protobuf-compiler -y`
- `CMD` a single instruction that provides a default command to be executed in the container
    - e.g. `CMD ["/bin/bash"]` or `CMD ["python3", "./setup.py"]` for multiple arguments

For an example of a valid Dockerfile, see [here](https://gist.github.com/chrislattman/a787857d4bbd3192ebe792c463379084).

## Creating images, containers, and volumes

- `docker pull image_name`
    - pulls a Docker image from [Docker Hub](https://hub.docker.com/)

- `docker build -t image_name .`
    - builds an image named `image_name` from a Dockerfile in the current directory

- `docker run -it [--name container_name] image_name [/bin/bash]`
    - starts up a container named `container_name` using the image `image_name` and running a bash shell
    - if the image already includes a shell as specified in its Dockerfile, the `/bin/bash` command can be removed without any effect
    - replacing `-it` with `-d` makes the container run detached (runs in the background)
        - use `-dt` instead of `-d` to prevent the container from shutting down upon exiting the terminal (it will need to be stopped by running `docker stop container_name`)
    - Optional: use `-p 8080:80` to bind port 8080 of the host computer to port 80 of the container
        - E.g. if the container is running a HTTP server (they use port 80 by [default](https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers#Well-known_ports)), you would be able to access the server via 127.0.0.1:8080
        - The purpose of port binding is to not interfere with another service running on 127.0.0.1:80
    - Optional: use `--privileged` to run this container with extended privileges (e.g. to allow some syscalls to be executed)
    - Optional: use `--rm` to automatically remove the container when it exits

- `docker run -it [--name container_name] --mount src=volume_name,dst=/data image_name [/bin/bash]`
    - starts up a container named `container_name` using the image `image_name` running a bash shell
    - mounts the volume `volume_name` to directory `/data` of the container

- `docker run -it [--name container_name] -v “$(pwd)”:/data image_name [/bin/bash]`
    - like the previous command, except it mounts the current local directory as a bind mount (similar to a volume) to directory `/data` of the container
    - for Windows hosts using PowerShell, replace `$(pwd)` with `${PWD}`
    - for Windows hosts using Command Prompt (not recommended), replace `$(pwd)` with `%cd%`
    - Optional: use `-w "$(pwd)"` to set the working directory to the value of `pwd`

- `docker volume create volume_name`
    - creates a new volume named `volume_name`

## Interacting with containers

- `docker exec container_name ls`
    - runs the `ls` command in a container

- `docker exec -it container_name /bin/bash`
    - runs a bash shell in a container, keeping the terminal open

- `docker cp container_name:/path/to/file.txt .`
    - copies `file.txt` from a container to the current local directory
    - works for both files and directories

- `docker cp file.txt container_name:/path/to/dir`
    - copies `file.txt` from your current directory to the `/path/to/dir` directory in a container
    - works for both files and directories

## Stopping containers

- `docker restart container_name`
    - restarts a running container, or starts up a previously stopped container

- `docker stop container_name`
    - stops a running container

## Informational commands

- `docker images`
    - shows the list of images
    - `docker images -a` shows all images (including dangling images)

- `docker ps`
    - shows all running containers
    - `docker ps -a` shows all containers (running and stopped)

- `docker volume ls`
    - shows all volumes

- `docker logs -f container_name`
    - prints out the logs for a running container

## Removing images, containers, and volumes

- `docker rmi image_name`
    - removes an image
    - instead of `image_name`, you can also reference an image by its image ID
        - you can use as few characters as possible from the image ID that uniquely identifies the image you want to remove
        - run `docker images -a` to see all of the images and their corresponding image IDs

- `docker image prune`
    - removes unused images
    - `docker image prune -a` removes all unused images

- `docker rm container_name`
    - removes a stopped container
    - instead of `container_name`, you can also reference a container by its container ID
        - you can use as few characters as possible from the container ID that uniquely identifies the container you want to remove
        - run `docker ps -a` to see all of the containers and their corresponding container IDs
    - `docker rm -f container_name` stops a running container and then removes it

- `docker container prune`
    - removes all stopped containers

- `docker volume rm volume_name`
    - removes a volume

- `docker volume prune`
    - removes all unused volumes

## Docker Compose

Docker Compose is a tool used to manipulate multi-container applications.

A compose file (usually named `docker-compose.yml`) outlines the services used by a multi-container application.

- A service is a component based on a Docker container
- Like containers, services are based on Docker images
    - They can either be built from a specified Dockerfile, can be a pre-built image from `docker images`, or can come from [Docker Hub](https://hub.docker.com/)
- Services can optionally have ports exposed, volumes mounted, environment variables set, default commands, depend on other services, etc.
- When a Docker Compose application is started, each service appears as a container
    - This can be verified this by running `docker ps` afterwards

Docker Compose commands:

- `docker compose build`
    - only builds services based on the compose file in the current directory

- `docker compose up`
    - builds and starts services from the compose file in the current directory
    - `docker compose up -d` does the same thing, but runs the application in detached mode

- `docker compose stop`
    - only stops all services specified in the compose file in the current directory
    - `Ctrl + C` does the same thing, but only works if the application is _not_ in detached mode

- `docker compose down`
    - stops all services specified in the compose file in the current directory, and removes all associated containers
    - `docker compose down --volumes` also deletes any volumes used by the services
