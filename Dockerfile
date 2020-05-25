# How to run :
# docker build -t prawinn555/javascriptshell:latest .
# make sur the directory exist C:/dev/volume/jsshell/workspace
# docker run -v C:/dev/volume/jsshell/workspace:/home/workspace -ti --rm --name jsshell prawinn555/javascriptshell:latest

FROM maven:3-jdk-11-slim as builder

WORKDIR /home
COPY pom.xml .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml dependency:resolve-plugins dependency:resolve clean

# When there are no changes in pom.xml file, docker will skip to this step directly
COPY src ./src
RUN echo "Copied files : " ; ls -R
RUN mvn compile assembly:single
RUN echo "Assembly file : " ; ls target/*.jar

FROM openjdk:13-jdk-alpine
RUN apk add --no-cache bash
# Set the working directory.
WORKDIR /home

# Copy the file from your host to your current location.
COPY --from=builder /home/target/*.jar ./
COPY *.txt ./
# copy test script
COPY test ./test

RUN echo "copied files" ; ls -Rl
RUN echo "----- testing application --------"
RUN java -Da=2 -jar ./javascriptshell-1.0-jar-with-dependencies.jar test/testScriptBash.js
RUN echo "----- end testing application --------"

VOLUME ["/home/workspace"]

RUN echo "cat /home/welcomeFile.txt" > ~/.bashrc
RUN ls -l ~/.bashrc
RUN echo "END build"
CMD ["bash" ]

