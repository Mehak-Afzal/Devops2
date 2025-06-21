# Start from a Java base image
FROM maven:3.9.4-eclipse-temurin-17

# Install Chrome & ChromeDriver
RUN apt-get update && apt-get install -y \
    wget gnupg unzip curl \
    chromium chromium-driver

# Set environment variables
ENV CHROME_BIN=/usr/bin/chromium

# Copy Maven project into container
COPY selenium-tests /app
WORKDIR /app

# Build tests ahead of time (optional)
RUN mvn clean install
