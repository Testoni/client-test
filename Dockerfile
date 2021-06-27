FROM openjdk:8-jdk-alpine
COPY target/*.jar client.jar
EXPOSE 8080
ENTRYPOINT ["java","-Duser.timezone=America/Sao_Paulo","-jar","/client.jar"]