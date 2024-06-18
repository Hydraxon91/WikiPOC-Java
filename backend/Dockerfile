# Use OpenJDK 11 as the base image
FROM openjdk:17-jdk-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY target/backend-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 80
# Run the application when the container starts
CMD ["java", "-jar", "/app/app.jar"]