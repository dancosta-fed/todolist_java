FROM ubuntu:lastest AS build

RUN apt-get update && apt-get install -y build-essential
RUN apt-get install openjdk-17-jdk -y

COPY . .

RUN apt-get install maven -y
RUN mvn clean install

EXPOSE 8080

COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]