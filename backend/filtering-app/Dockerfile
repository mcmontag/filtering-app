FROM maven:3.6.3 AS maven

WORKDIR /usr/src/app

COPY . /usr/src/app

COPY ./src/main/resources/application.properties /opt/app/application.properties

RUN mvn package

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /opt/app

COPY --from=maven /usr/src/app/target/filtering-app-0.0.1-SNAPSHOT.jar /opt/app/

ENTRYPOINT ["java", "-jar", "filtering-app-0.0.1-SNAPSHOT.jar"]
