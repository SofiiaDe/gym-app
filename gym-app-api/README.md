Swagger UI: http://localhost:8080/swagger-ui/index.html \
Health Indicators: http://localhost:8080/actuator/health \
Prometheus: http://localhost:8080/actuator/prometheus \
Eureka: http://localhost:8761 \
SonarQube: http://localhost:9000/ \
Jenkins: http://localhost:9090/

SonarQube analyze
mvn clean verify sonar:sonar \
