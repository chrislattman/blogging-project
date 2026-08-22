FROM eclipse-temurin:25
WORKDIR /blogging-project
COPY src ./src
RUN javac -d bin src/app/*.java
WORKDIR /blogging-project/bin
CMD ["java", "app/Driver"]
