FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewm_app.jar
ENTRYPOINT ["java","-jar","/ewm_app.jar"]