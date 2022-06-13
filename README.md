# spring-boot-coding-assignment

## Introduction

This service implements a JSON REST API that can be used to validate Social Security Numbers that are in a format
enforced by the Finnish government.

## Requirements

The service is built using following tools and frameworks:

- Java 11
- Spring Boot 2.7.0
- Gradle
- Git
- Docker

## Development

In this section you can find information about setting up a local development environment.

### Cloning the repository

Source code is stored in [Github](https://github.com/kuusemi/spring-boot-coding-assignment) and can be cloned into a
local development machine using git command line tool, using a following command:

`git clone https://github.com/kuusemi/spring-boot-coding-assignment`

Project can then be imported to the IDE of your choice. For example using IntelliJ IDEA by selecting from **File** menu
option **New** and from under that option **Project from Existing Sources...**

Alternatively the source code can be cloned directly to IntelliJ IDEA by selecting from **File** menu
option **New** and from under that option **Project from Version Control...**

Other integrated development environments, e.g. Eclipse has similar functionality available.

After the source code has been imported, project dependencies are automatically loaded to the local Gradle cache.

### Building executable jar file from the command line

Readily available executable can be build from the command line with a following command:

`./gradlew bootJar`

After the jar file has been built, it can be started from the command line:

`java -jar build/libs/spring-boot-coding-assignment-0.0.1-SNAPSHOT.jar `

By default, application will start listening on port 8090.

### Running unit tests

Unit tests can be executed from the command line using the following command:

`./gradlew test`

### Containerising the application

The repository contains a Docker build file that can be used to build a container image from the source. Docker image
can be built with the following command from the command line:

`docker build -t mkuusela/spring-boot-coding-assignment:latest .`

The -t option is optional and the command can be executed without it. The command runs unit tests, builds a bootable jar
file and creates a container image that can then be used to start a container by issuing a following command from the command line:

`docker run -d --rm -p 8090:8090 --name spring-code-assignment mkuusela/spring-boot-coding-assignment:latest`

The aforementioned command starts up the application and exposes port 8090 to the host operating system. Application
logs can be accessed with the following command:

`docker logs --follow spring-code-assignment`

Application can be stopped with the following command:

`docker stop spring-code-assignment`

## Testing the application

The application can be tested using curl command line tool with following commands:

Successful request:

`
curl -v -X POST -H "Content-type: application/json" -d '{"ssn": "010507A632V", "countryCode": "FI"}' http://localhost:8090/validate_ssn/
`

Success response

`
{
"ssn_valid": true
}
`

Failed request:

`
curl -v -X POST -H "Content-type: application/json" -d '{"ssn": "010507A632V", "countryCode": "SE"}' http://localhost:8090/validate_ssn/
`

Failure response

`
{
"ssn_valid": false
}
`
