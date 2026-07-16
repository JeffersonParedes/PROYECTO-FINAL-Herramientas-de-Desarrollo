# --- Etapa 1: Build ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero el pom.xml para cachear dependencias
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN mvn dependency:go-offline -B

# Ahora copiamos el código y compilamos
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Etapa 2: Runtime ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]