# Use a base image with Java installed
FROM openjdk:22-jdk

# Set the working directory in the container
WORKDIR /app
ARG JAR_FILE=target/*.jar
# Copy the Spring Boot application JAR file to the container
COPY ./target/VoltApp-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app
RUN chmod 777 /app
# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Set the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
