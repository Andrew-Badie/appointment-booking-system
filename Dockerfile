FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /src
COPY pom.xml ./
COPY services ./services
RUN mvn --batch-mode --no-transfer-progress clean verify

FROM tomcat:9.0-jdk11-temurin AS runtime
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
ENV JAVA_OPTS="-Xms64m -Xmx256m"
COPY deployment/docker/start-app.sh /usr/local/bin/start-app
RUN chmod +x /usr/local/bin/start-app
CMD ["/usr/local/bin/start-app"]

FROM runtime AS frontend
COPY --from=build /src/services/Frontend/target/FrontEnd-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/FrontEnd.war
FROM runtime AS search
COPY --from=build /src/services/SearchAppointments/target/SearchAppointments-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/SearchAppointments.war
FROM runtime AS book
COPY --from=build /src/services/BookAppointment/target/BookAppointment-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/BookAppointment.war
FROM runtime AS confirm
COPY --from=build /src/services/ConfirmAppointment/target/ConfirmAppointment-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ConfirmAppointment.war
