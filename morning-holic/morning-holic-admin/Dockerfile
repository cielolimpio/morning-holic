FROM adoptopenjdk/openjdk11

RUN mkdir /opt/app
COPY build/libs/morning-holic-admin.jar /opt/app/morning-holic-admin.jar
CMD ["java", "-Denv=dev", "-jar", "/opt/app/morning-holic-admin.jar", "--spring.profiles.active=dev"]
