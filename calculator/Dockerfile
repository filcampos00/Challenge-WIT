FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /project
COPY . .
RUN mvn -DskipTests -pl calculator -am clean package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app
COPY --from=build /project/calculator/target/*.jar app.jar
RUN mkdir logs
RUN chown -R spring:spring /app
USER spring:spring
ENTRYPOINT ["java","-jar","app.jar"]