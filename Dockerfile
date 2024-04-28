
# Use a JDK 17 slim image for the final image to reduce size
FROM openjdk:17-slim

# Set the working directory for the runtime environment
WORKDIR /app

# Copy the built JAR file from the build stage
COPY target/*.jar /app/app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app/app.jar"]
