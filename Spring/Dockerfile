FROM openjdk:17-slim
WORKDIR /app
COPY . /app
RUN mvnw clean install -DskipTests
EXPOSE 8080
EXPOSE 5432
CMD ["./mvnw","spring-boot:run"]