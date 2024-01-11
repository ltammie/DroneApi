This is a drone REST API application. See below how to compile and run it

### Prerequisites

- java 21
- docker (optional)

### To compile use the command below:

- ./mvnw clean install

### To compile and run a SpringBoot application use:

- ./mvnw spring-boot:run

### You can open swagger ui to send request via friendly interface

- http://localhost:8080/drone-management/swagger.html

### Optional

#### You can use following commands to set up prometheus and grafana to monitor drone application
#### To start prometheus and grafana use:

- docker compose -f monitoring/docker-compose.yml up

#### Note: You should have docker installed, configured in your system and running

#### To see a dashboard showing drones battery:

1. Go to grafana http://localhost:3000/
2. Sign in using admin and admin (login and password respectively)
3. Go to Home > DashBoards and open a dashboard with name DroneBatteryLevel

grafana url: http://localhost:3000/
prometheus url: http://localhost:9090/

