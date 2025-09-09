FROM gradle:9.0-jdk24 AS build
WORKDIR /travelPlanner
COPY . .
WORKDIR /travelPlanner/backend
RUN gradle assemble

FROM openjdk:24-slim
WORKDIR /travelPlanner
COPY --from=build /travelPlanner/backend/build/libs/*.jar app.jar
COPY --from=build /travelPlanner/env.prod.properties env.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]