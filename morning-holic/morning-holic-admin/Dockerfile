FROM adoptopenjdk/openjdk11:x86_64-debian-jdk-11.0.5_10-slim

RUN mkdir /opt/app
COPY build/libs/morning-holic-admin.jar /opt/app/morning-holic-admin.jar
CMD ["java", "-Denv=dev", "-jar", "/opt/app/morning-holic-admin.jar", "--spring.profiles.active=dev"]
