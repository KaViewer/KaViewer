FROM maven:3.6.0-jdk-11-slim AS package
WORKDIR /kaviewer
COPY ../ .
RUN mvn -f /kaviewer/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=package /kaviewer/app/target/app-0.0.1.jar /app/kaviewer.jar
EXPOSE 9394
ENTRYPOINT ["java","-jar","/app/kaviewer.jar"]