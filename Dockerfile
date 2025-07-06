# build
FROM maven:3.8.8-amazoncorretto-21-al2023 AS build
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

# run
FROM amazoncorretto:21.0.5
WORKDIR /app

COPY --from=build ./build/target/*jar ./app.jar

EXPOSE 8080
# EXPOSE 9090 (esse é para actuator, se tiver)

# variáveis de ambiente
ENV DATASOURCE_URL=''
ENV DATASOURCE_USER=''
ENV DATASOURCE_PASSWORD=''

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

# executa o jar
ENTRYPOINT java -jar app.jar
