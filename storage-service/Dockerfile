FROM maven:3.8.1-jdk-11

WORKDIR /usr/src/app

COPY . /usr/src/app
RUN mvn package

ENV PORT 7631
EXPOSE $PORT
CMD [ "sh", "-c", "mvn -Dserver.port=${PORT} spring-boot:run" ]
