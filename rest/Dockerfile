FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /project
COPY . .
RUN mvn -DskipTests -pl rest -am clean package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app
COPY --from=build /project/rest/target/*.jar app.jar
RUN chown -R spring:spring /app
USER spring:spring
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]