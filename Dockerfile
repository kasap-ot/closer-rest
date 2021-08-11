FROM openjdk:11

EXPOSE 8080

ADD target/closer-with-rest-api.jar closer-with-rest-api.jar

ENTRYPOINT ["java", "-jar", "/closer-with-rest-api.jar"]