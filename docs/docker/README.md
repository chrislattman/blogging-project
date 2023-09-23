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
- [Pushing images to a Docker image library](#pushing-images-to-a-docker-image-library)
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

Congratulations! You now have terminal access to a bare-bones Ubuntu. You can try experimenting with some [terminal commands](../terminal-commands#the-43-most-important-terminal-commands-plus-symbols-and-compression). When you're done, you can exit the Ubuntu container by running `exit`.

## Docker image vs. Docker container vs. Docker volume

- An image is an immutable file that contains files needed to run a container
- A container is a runtime environment that runs on top of an image
    - Unlike virtual machines, they virtualize at the application layer and share the same underlying "OS" (container engine)
- A volume is a file system (a directory) mounted to a container
    - This allows data to be backed up and saved, regardless of container status
    - This also allows data to be shared between containers

## Dockerfile

- A script of instructions that tells Docker how to build an image
- `FROM` defines the base of the image, usually a parent image
    - e.g. `FROM [custom_repository/]ubuntu[:latest]`
- Optional: `WORKDIR` sets the working directory
    - e.g. `WORKDIR /code`
- Optional: `COPY` copies a file from the current host directory to the image
    - e.g. `COPY file.txt .`
- Optional: `EXPOSE` exposes a port to the host OS
    - e.g. `EXPOSE 80/tcp`
    - This only allows other Docker containers to access this port on a given container
    - This is made unnecessary when using the `-p` flag for `docker run`
- `RUN` specifies commands to run while building an image in the layer on top of it
    - e.g. `RUN apt update && apt install cmake protobuf-compiler -y`
- `CMD` a single instruction that provides a default command to be executed in the container
    - e.g. `CMD ["/bin/bash"]` or `CMD ["python3", "./setup.py"]` for multiple arguments

For an example of a valid Dockerfile, see [here](https://gist.github.com/chrislattman/a787857d4bbd3192ebe792c463379084).

## Creating images, containers, and volumes

- `docker pull image-name`
    - Pulls the latest version of a Docker image from [Docker Hub](https://hub.docker.com/)

- `docker build -t image-name .`
    - Builds an image named `image-name` from a Dockerfile in the current directory

- `docker run -it [--name container-name] [other flags] image-name [/bin/bash]`
    - Starts up a container named `container-name` using the image `image-name` and running a bash shell
    - If the image already includes a shell as specified in its Dockerfile, the `/bin/bash` command can be removed without any effect
    - Replacing `-it` with `-d` makes the container run detached (runs in the background)
        - Use `-dt` instead of `-d` to prevent the container from shutting down upon exiting the terminal (it will need to be stopped by running `docker stop container-name`)
    - Optional: use `-p 8080:80/tcp` to bind port 8080 of the host computer to port 80 of the container over TCP (UDP is available too)
        - E.g. if the container is running a HTTP server (they use port 80 by [default](https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers#Well-known_ports)), you would be able to access the server via 127.0.0.1:8080
        - If no protocol is specified (e.g. `/tcp`), then TCP is assumed
        - The purpose of port forwarding is to allow programs running on the host computer to communicate with Docker containers
    - Optional: use `--privileged` to run this container with extended privileges (e.g. to allow some syscalls to be executed)
    - Optional: use `--rm` to automatically remove the container when it exits
    - Optional: use `--cpus <num-cpus>` to specify how many CPUs the container can use, e.g. `--cpus 4`
    - Optional: use `-m <memory>` to specify how much memory the container can use, e.g. `-m 4GB`

- `docker run -it [--name container-name] -v volume-name:/data image-name [/bin/bash]`
    - Starts up a container named `container-name` using the image `image-name` running a bash shell
    - Mounts the volume `volume-name` to directory `/data` of the container

- `docker run -it [--name container-name] -v “$(pwd)”:/data image-name [/bin/bash]`
    - Like the previous command, except it mounts the current local directory as a bind mount (similar to a volume) to directory `/data` of the container
    - For Windows hosts using PowerShell, replace `$(pwd)` with `${PWD}`
    - For Windows hosts using Command Prompt (not recommended), replace `$(pwd)` with `%cd%`
    - Optional: use `-w "$(pwd)"` to set the working directory to the value of `pwd`
    - If instead of `"$(pwd)`, you specify another directory that does not exist yet, Docker will create it for you

- `docker volume create volume-name`
    - Creates a new volume named `volume-name`

- `docker commit container-name snapshot-image-name`
    - Creates a snapshot of container `container-name` and saves it as the image `snapshot-image-name`
    - Note: this does not save any data from volumes

- `docker rename container-name new-name`
    - Renames a container to the new name specified
    - This can be helpful if you forgot to name your container, or it just needs a new name

Unfortunately, there is currently no `docker volume` command to clone or even rename a volume. However, you can accomplish this by running the following commands:

```
docker volume create clone-volume
docker run --rm -v test-volume:/data -v clone-volume:/data2 image-name sh -c 'cp -r /data/* /data2'
```

- This creates a clone of some existing Docker volume `test-volume` in `clone-volume`

## Interacting with containers

- `docker exec container-name ls`
    - Runs the `ls` command in a container

- `docker exec -it container-name /bin/bash`
    - Runs a bash shell in a container, keeping the terminal open

- `docker cp container-name:/path/to/file.txt .`
    - Copies `file.txt` from a container to the current local directory
    - Works for both files and directories

- `docker cp file.txt container-name:/path/to/dir`
    - Copies `file.txt` from your current directory to the `/path/to/dir` directory in a container
    - Works for both files and directories

## Stopping containers

- `docker restart container-name`
    - Restarts a running container, or starts up a previously stopped container

- `docker stop container-name`
    - Stops a running container

## Informational commands

- `docker images`
    - Shows the list of images
    - `docker images -a` shows all images (including dangling images)

- `docker ps`
    - Shows all running containers
    - `docker ps -a` shows all containers (running and stopped)

- `docker volume ls`
    - Shows all volumes

- `docker logs -f container-name`
    - Prints out the logs for a running container

## Removing images, containers, and volumes

- `docker rmi image-name`
    - Removes an image
    - Instead of `image-name`, you can also reference an image by its image ID
        - You can use as few characters as possible from the image ID that uniquely identifies the image you want to remove
        - Run `docker images -a` to see all of the images and their corresponding image IDs

- `docker image prune`
    - Removes unused images
    - `docker image prune -a` removes all unused images

- `docker rm container-name`
    - Removes a stopped container
    - Instead of `container-name`, you can also reference a container by its container ID
        - You can use as few characters as possible from the container ID that uniquely identifies the container you want to remove
        - Run `docker ps -a` to see all of the containers and their corresponding container IDs
    - `docker rm -f container-name` stops a running container and then removes it

- `docker container prune`
    - Removes all stopped containers

- `docker volume rm volume-name`
    - Removes a volume

- `docker volume prune`
    - Removes all unused volumes

## Pushing images to a Docker image library

You may want to share your image on Docker Hub or some other Docker image library. This will allow others to use your image (and is a major benefit of containerization).

These instructions will vary depending on what image library you're pushing to. If you're using Docker Hub, here are some initial instructions:

1. If you don't already have an account, create an account on [Docker Hub](https://hub.docker.com/) and set up 2FA
1. Create a Docker Hub [repository](https://hub.docker.com/repository/create) for the image you want to push
1. Login to Docker Desktop with your Docker Hub account

To push an image:

1. If you aren't using Docker Hub, authenticate using the `docker login` command:
    ```
    docker login -u <username> -p <password> <image-library-url>
    ```
    - You may need to specify a port number followed by a possible subdomain for the URL
    - You may need to use a personal access token instead of your password
    - You should store your password/token as a temporary [environment variable](https://www.freecodecamp.org/news/how-to-set-an-environment-variable-in-linux#how-to-set-environment-variables-in-linux) and reference that variable in this command in order to hide it from your command history
1. After you have [built](#creating-images-containers-and-volumes) your image, run
    ```
    docker tag image-name <docker-hub-username>/<docker-hub-repository-name>:<version>
    ```
    This will tag the image you just built in a format that is acceptable to Docker Hub.
    - You could have also just built the image by specifying the image name as `<docker-hub-username>/<docker-hub-repository-name>:<version>`
    - Change the image name as necessary if using something other than Docker Hub
1. Push the image by running
    ```
    docker push <docker-hub-username>/<docker-hub-repository-name>:<version>
    ```

The latest version of an image should be tagged with version `latest` and pushed.

If you need to use another image library, make sure to run `docker logout <image-library-url>`.

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
    - Only builds services based on the compose file in the current directory

- `docker compose up`
    - Builds and starts services from the compose file in the current directory
    - `docker compose up -d` does the same thing, but runs the application in detached mode

- `docker compose stop`
    - Only stops all services specified in the compose file in the current directory
    - `Ctrl + C` does the same thing, but only works if the application is _not_ in detached mode

- `docker compose down`
    - Stops all services specified in the compose file in the current directory, and removes all associated containers
    - `docker compose down --volumes` also deletes any volumes used by the services
