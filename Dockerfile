FROM openjdk:17-alpine
EXPOSE 8080
COPY calendar_api-0.0.1-SNAPSHOT.jar .
RUN apk add --no-cache bash
COPY wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh
ENTRYPOINT ["/wait-for.sh", "postgres_container:5432", "--timeout=20", "--", "java", "-jar", "calendar_api-0.0.1-SNAPSHOT.jar"]