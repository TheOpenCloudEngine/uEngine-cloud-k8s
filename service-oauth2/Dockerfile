# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY run.sh run.sh
COPY cloud-run-1-cb2c560a9e31.json cloud-run-1-cb2c560a9e31.json
COPY keystore.p12 keystore.p12

# Build a release artifact.
RUN mvn package -DskipTests


# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk8:jdk8u202-b08-alpine-slim

# Copy the jar to the production image from the builder stage.

COPY --from=builder /app/target/service-oauth2-*.jar /service-oauth2.jar
#COPY --from=builder /app/cloud_sql_proxy /cloud_sql_proxy
COPY --from=builder /app/run.sh /run.sh
COPY --from=builder /app/cloud-run-1-cb2c560a9e31.json /cloud-run-1-cb2c560a9e31.json
COPY --from=builder /app/keystore.p12 /keystore.p12

RUN wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy
RUN chmod +x cloud_sql_proxy

EXPOSE 3307
EXPOSE 8081

#CMD ["sh", "./run.sh"]

#ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]
# Run the web service on container startup.
CMD ["sh", "./run.sh"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./service-oauth2.jar", "--spring.profiles.active=docker"]
#CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=${PORT}","-jar","/service-oauth2.jar"]
