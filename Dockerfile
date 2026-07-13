FROM apache/beam_java21_sdk:2.73.0

WORKDIR /opt
COPY target/*.jar /opt/app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/opt/app.jar" ]