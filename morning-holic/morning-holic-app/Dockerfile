FROM adoptopenjdk/openjdk11

RUN mkdir /opt/app
COPY build/libs/morning-holic-app.jar /opt/app/morning-holic-app.jar
CMD ["java", "-Denv=dev", "-jar", "/opt/app/morning-holic-app.jar", "--spring.profiles.active=dev"]
