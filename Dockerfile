FROM openjdk:latest
WORKDIR /blogging-project
COPY src ./src
RUN javac -d bin src/app/*.java
WORKDIR /blogging-project/bin
CMD ["java", "app/Driver"]
