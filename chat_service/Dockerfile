# First stage: Build the application
FROM maven:3.8.3-openjdk-17 AS builder

# Copy the application source code and build the JAR
COPY ./src/ /root/src
COPY ./pom.xml /root/
COPY ./checkstyle.xml /root/
WORKDIR /root
RUN mvn package -DskipTests

# Copy the JAR to a designated directory
RUN mkdir -p /developments
ADD ./target/chat_service-0.0.1-SNAPSHOT.jar /developments/

# Expose the port (this will affect the image metadata)
EXPOSE 8083

# Second stage: Create the final runtime image
FROM openjdk:17-jdk-slim-buster

# Set the timezone (optional)
ENV TZ=UTC

# Copy dependencies and JAR from the builder stage
COPY --from=builder /developments/chat_service-0.0.1-SNAPSHOT.jar /app/chat_service-0.0.1-SNAPSHOT.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/chat_service-0.0.1-SNAPSHOT.jar", "-XX:+UseContainerSupport", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:MaxRAMFraction=1", "-Xms512m", "-Xmx512m", "-XX:+UseG1GC", "-XX:+UseSerialGC", "-Xss512k", "-XX:MaxRAM=72m"]
