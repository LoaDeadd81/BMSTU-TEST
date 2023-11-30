FROM openjdk:20-jdk
RUN mkdir /app
RUN microdnf install findutils
RUN mkdir -p /app/allure-results /app/res/BL /app/res/DA /app/res/RestAPI /app/res/main
ENTRYPOINT ["tail", "-f", "/dev/null"]