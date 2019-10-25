FROM openjdk:11-jdk-slim
VOLUME /tmp
ADD target/*.jar job-service.jar
ARG ACTIVE_PROFILE
ARG JAVA_OPTS
ENV ACTIVE_PROFILE=${ACTIVE_PROFILE}
ENV JAVA_OPTS=${JAVA_OPTS}

EXPOSE 8080

ENTRYPOINT java ${JAVA_OPTS} -Dreactor.netty.http.server.accessLogEnabled=true \
            -Dspring.profiles.active=${ACTIVE_PROFILE:-dev} -Djava.security.egd=file:/dev/./urandom \
            -jar /job-service.jar
