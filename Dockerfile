# parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Package the application
RUN ./mvnw package

# Run the jar file
CMD ["java", "-jar", "target/invoices-0.0.1-SNAPSHOT.jar"]
