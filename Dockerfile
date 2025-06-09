# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Criar usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar o JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Variáveis de ambiente
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE="prod"

# Expor a porta
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 