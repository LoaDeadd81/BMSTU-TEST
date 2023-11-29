FROM openjdk:20-jdk
RUN mkdir /app
RUN microdnf install findutils
ENTRYPOINT ["java","-jar","/app/build/libs/KApp-1.0-SNAPSHOT.jar"]