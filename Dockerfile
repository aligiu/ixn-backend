# Use a Maven image with JDK 22 to build the jar
FROM maven:3.8.5-eclipse-temurin-22 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use an Eclipse Temurin JDK 22 runtime image to run the application
FROM eclipse-temurin:22-jre
WORKDIR /app

# Copy the jar file from the Maven build stage
COPY --from=build /app/target/ixn-0.0.1-SNAPSHOT.jar ixn-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/ixn-0.0.1-SNAPSHOT.jar"]
