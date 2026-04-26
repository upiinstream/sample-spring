# -------- build stage --------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy POM 
COPY pom.xml .
RUN mvn dependency:go-offline


# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# -------- runtime stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the app jar
ARG JAR_FILE=/app/target/*.jar

COPY --from=build /app/target/*.jar /app/sample-spring.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/sample-spring.jar"]







