# Use a Maven image with JDK 17 to build the jar
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use an openjdk 17 runtime image to run the application
FROM  openjdk:17.0.1-jdk-slim
WORKDIR /app

# Copy the jar file from the Maven build stage
COPY --from=build /app/target/ixn-0.0.1-SNAPSHOT.jar ixn-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/ixn-0.0.1-SNAPSHOT.jar"]
