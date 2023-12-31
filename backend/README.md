## Microservices Overview
1. api-gateway: The entry point for all LMS backend services, routing requests to appropriate services.
2. auth-service: Manages authentication and authorization.
3. config-server: Centralized configuration service for managing configurations across services.
4. discovery-server: Service registry for registering all services and enabling them to discover and communicate with each other.
5. exam-server: Handles functionalities related to exams and assessments.
6. faculty-server: Manages faculty-related operations.
7. subject-server: Deals with subjects and course material management.


## Prerequisites
Before you start the services, ensure you have the following installed:

1. Java (JDK 11 or higher)
2. Maven
3. Docker
4. Any IDE of your choice (IntelliJ IDEA, Eclipse, etc.)
5. Git (for cloning the repository)
6. Docker 

We have a Docker setup to manage MySQL instances for different services using Docker Compose. This setup is defined in a docker-compose.yml file with the following services:

1. database-auth-dev
2. database-faculty-dev
3. database-subject-dev
4. database-exam-dev

Each service runs a MySQL instance on a separate port and has its own volume for data persistence.

## Starting the MySQL Containers
To start the MySQL instances, run the following command in the directory containing the docker-compose.yml file:

`docker-compose up -d`

This will start the MySQL instances in detached mode.

## Starting the Backend Services
Certain services must be started before others to ensure proper registration and configuration.

Start config-server and discovery-server before other services. Then start the api-gateway. After the above services are up, start auth-service, exam-server, faculty-server, and subject-server.

EduMorph utilizes a microservices architecture, and each microservice is containerized using Docker. Additionally, service-specific configurations are managed through YAML files located in the conf folder.

Each microservice in our LMS has a dedicated Dockerfile, which defines the environment and steps to build and run the service. The Dockerfile uses a two-stage build process for efficient image creation.

## Starting Services with Docker
To start a service, build the Docker image using the Dockerfile and run the container. Ensure Docker is installed and running on your system. For example, to start the exam-service:

`docker build -t exam-service`

`docker run -d -p [Your Desired Port]:[Service Port] exam-service`
