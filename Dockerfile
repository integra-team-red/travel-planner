FROM gradle:9.1-jdk24 AS backend_build
WORKDIR /travelPlanner
COPY . .
WORKDIR /travelPlanner/backend
RUN gradle assemble

FROM openjdk:24-slim
WORKDIR /travelPlanner
COPY --from=backend_build /travelPlanner/backend/build/libs/*.jar app.jar
COPY --from=backend_build /travelPlanner/env.prod.properties env.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
