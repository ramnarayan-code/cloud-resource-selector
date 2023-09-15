FROM openjdk:17-alpine as build

ARG MAVEN_VERSION=3.9.4
ARG USER_HOME_DIR="/root"
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN apk --update --no-cache add curl

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
 && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
 && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
 && rm -f /tmp/apache-maven.tar.gz \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# # Define working directory.
# WORKDIR /data
#
# # Define commonly used JAVA_HOME variable
# ENV JAVA_HOME /usr/lib/jvm/default-jvm/

ENV HOME=/usr/app

RUN mkdir -p $HOME

WORKDIR $HOME

ADD . $HOME

RUN mvn package


FROM openjdk:17-alpine
COPY --from=build /usr/app/target/cloud-resource-selector-0.0.1-SNAPSHOT.jar /app/runner.jar
ENTRYPOINT java -jar /app/runner.jar