FROM openjdk:20-jdk
RUN mkdir /app
RUN microdnf install findutils
COPY kapp /app
ENTRYPOINT ["tail", "-f", "/dev/null"]