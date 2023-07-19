FROM adoptopenjdk/openjdk11:x86_64-debian-jdk-11.0.5_10-slim

RUN mkdir /opt/app
COPY build/libs/morning-holic-app.jar /opt/app/morning-holic-app.jar
CMD ["java", "-Denv=dev", "-jar", "/opt/app/morning-holic-app.jar", "--spring.profiles.active=dev"]
