FROM node:20 AS frontend_build
WORKDIR /travelPlanner/frontend
COPY frontend/package.json ./
COPY frontend/package-lock.json ./
RUN npm ci
COPY frontend/ .
RUN npm run build

FROM gradle:9.0-jdk24 AS backend_build
WORKDIR /travelPlanner
COPY . .
# copy over the build frontend to a pre-configured public SpringBoot path
COPY --from=frontend_build /travelPlanner/frontend/dist /travelPlanner/backend/src/main/resources/static
WORKDIR /travelPlanner/backend
RUN gradle clean assemble

FROM openjdk:24-slim
WORKDIR /travelPlanner
COPY --from=backend_build /travelPlanner/backend/build/libs/*.jar app.jar
COPY --from=backend_build /travelPlanner/env.prod.properties env.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
