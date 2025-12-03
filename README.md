# Auth Service

## Overview

The **Auth Service** is a core microservice in the Food Ordering System responsible for user authentication and authorization. It provides JWT-based authentication, user registration, and user management capabilities.

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Security**: JWT-based authentication
- **Spring Data JPA**: Database operations
- **MySQL**: Database (auth_db)
- **RabbitMQ**: Message broker integration
- **Lombok**: Boilerplate code reduction
- **Springdoc OpenAPI**: API documentation (Swagger)

## Port

- **Service Port**: 8081
- **Database Port**: 33061 (MySQL)

## Database

- **Database Name**: `auth_db`
- **Tables**:
  - `users` - Stores user credentials and profile information

### User Schema
```sql
- id (Long, Primary Key)
- name (String)
- email (String, Unique)
- password (String, Encrypted)
- role (Enum: USER, ADMIN)
- created_at (Timestamp)
```

## API Endpoints

### Swagger Documentation
- **Swagger UI**: http://localhost:8081/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8081/v3/api-docs

### Public Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Response**:
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john@example.com",
  "role": "USER"
}
```

### Protected Endpoints

#### Get User Details
```http
GET /api/users/{userId}
Authorization: Bearer {jwt-token}
```

**Response**:
```json
{
  "id": 1,
  "username": "John Doe",
  "email": "john@example.com"
}
```

#### Health Check
```http
GET /api/auth/health
```

## Security

- **JWT Authentication**: Tokens are generated upon successful login
- **Password Encryption**: BCrypt password encoding
- **Token Expiration**: Configurable JWT expiration time
- **Role-Based Access**: USER and ADMIN roles supported

## Configuration

### application.properties

```properties
# Server Configuration
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:mysql://mysql_auth:3306/auth_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000

# RabbitMQ Configuration
spring.rabbitmq.host=rabbitmq
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## Building the Service

### Prerequisites
- Java 17+
- Maven 3.6+

### Build Commands

```bash
# Build the service
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run locally
mvn spring-boot:run
```

## Docker

### Dockerfile
The service includes a Dockerfile for containerization:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build Docker Image
```bash
docker build -t auth-service:latest .
```

### Run with Docker Compose
```bash
# From project root
docker-compose up auth-service
```

## Dependencies

Key dependencies include:
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-data-jpa` - Database operations
- `spring-boot-starter-actuator` - Health checks and metrics
- `spring-boot-starter-amqp` - RabbitMQ integration
- `mysql-connector-j` - MySQL driver
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` - JWT token handling
- `springdoc-openapi-starter-webmvc-ui` - Swagger documentation

## Project Structure

```
auth-service/
├── src/main/java/com/foodorder/auth/
│   ├── controller/          # REST controllers
│   ├── service/             # Business logic
│   ├── repository/          # Data access layer
│   ├── model/               # Entity classes
│   ├── dto/                 # Data transfer objects
│   ├── config/              # Configuration classes
│   │   ├── SecurityConfig.java
│   │   ├── JwtConfig.java
│   │   └── SwaggerConfig.java
│   └── AuthServiceApplication.java
├── src/main/resources/
│   └── application.properties
├── Dockerfile
├── pom.xml
└── README.md
```

## Inter-Service Communication

### Outbound
- None (Auth Service doesn't call other services)

### Inbound
- **Notification Service** → GET `/api/users/{userId}` (to fetch user details for emails)

## Testing

### Manual Testing with cURL

```bash
# Register a user
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "role": "USER"
  }'

# Login
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Via API Gateway

```bash
# Register via Gateway
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{...}'

# Login via Gateway
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{...}'
```

## Monitoring & Health

### Actuator Endpoints
- **Health**: http://localhost:8081/actuator/health
- **Info**: http://localhost:8081/actuator/info

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure MySQL container is running
   - Check database credentials in application.properties
   - Verify network connectivity

2. **JWT Token Invalid**
   - Check JWT secret key configuration
   - Verify token hasn't expired
   - Ensure token format is correct (Bearer {token})

3. **Port Already in Use**
   ```bash
   # Find process using port 8081
   lsof -i :8081
   # Kill the process
   kill -9 <PID>
   ```

## Environment Variables

When running in Docker, these can be overridden:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://mysql_auth:3306/auth_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SPRING_RABBITMQ_HOST=rabbitmq
JWT_SECRET=your-secret-key
```

## Contributing

When contributing to this service:
1. Follow Spring Boot best practices
2. Maintain constructor injection pattern
3. Use Lombok annotations appropriately
4. Update Swagger documentation for new endpoints
5. Add appropriate security annotations

## Related Documentation

- [Main Project README](../README.md)
- [API Documentation](../API_DOCUMENTATION.md)
- [Security Guide](../SECURITY.md)
- [Architecture Overview](../ARCHITECTURE.md)
- [Swagger Documentation](../SWAGGER_DOCUMENTATION.md)

## License

Part of the Food Ordering Microservices System - A demonstration project for microservices architecture.
