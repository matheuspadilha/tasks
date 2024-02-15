# Use an OpenJDK base image
FROM openjdk:21-minimal

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/tasks-0.0.1-SNAPSHOT.jar tasks.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/tasks.jar"]