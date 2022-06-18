# docker build -f docker/Dockerfile -t kooooooy/kaviewer:0.0.1 .
FROM openjdk:11-jre-slim
COPY /app/target/app-0.0.1.jar /app/kaviewer.jar
EXPOSE 9394
ENTRYPOINT ["java","-jar","/app/kaviewer.jar"]