FROM openjdk:21-jdk-oraclelinux8
WORKDIR /app
COPY target/boot_app-0.0.10-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]