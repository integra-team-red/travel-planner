FROM gradle:9.0-jdk24 AS build
WORKDIR /travelPlanner
COPY . .
WORKDIR /travelPlanner/backend
RUN gradle assemble

FROM node:20 AS frontend_build
WORKDIR /travelPlanner
COPY frontend/package*.json ./
RUN npm ci
WORKDIR /travelPlanner/frontend
COPY frontend .
RUN npm run build

FROM openjdk:24-slim
WORKDIR /travelPlanner
COPY --from=build /travelPlanner/backend/build/libs/*.jar app.jar
COPY --from=build /travelPlanner/env.prod.properties env.properties
COPY --from=frontend_build /travelPlanner/frontend/dist /public
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]