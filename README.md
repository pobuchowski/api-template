# Fuse Spring-Boot Arqullian Microservice QuickStart

This example demonstrates how you can use RedHat Fuse with Spring Boot and Arquillian to create and test Dockerised Microservices

# Building

The example can be built with
```
mvn clean install spring-boot:repackage docker:build
```

## Running the example locally

This project requires the stub-template image from this project
https://github.com/tim-m-robinson/stub-template

The easiest way to run this sample is using docker-compose

Run container: (docker-compose)
```
docker-compose up
```
If you wish to run the sample using 'raw' docker

Run container: (docker)
```
docker run -d --name stub stub-template:1.0-SNAPSHOT
docker run -d --name test -p 1080:1080 --link stub:stub.stub.io api-template:1.0-SNAPSHOT
```
## Query service 

Query running docker container:
```
curl http://localhost:1080/dummy/time
```

Expected response:
```
2018-08-12T19:07:37.644Z

```
## Running test
There are three types of tests configured in the pom file, integration tests started with `test` profile, unit tests started with `unit-test` profile and a variant of integration tests using the Arquillian Container-Object Model `com-test`

For all types of test we use random ports and container names to avoid conflicts in Continuous Integration environments where many build and test cycles are run in parallel.

### Unit test
Unit tests run tests in the JVM without Docker. The unit tests generate JaCoCo code coverage reports. They can be found in the target/site directory after a successful test run

To run the unit tests start: 
```
mvn test -P unit-test
```
### Integration tests
Integration tests build a Docker image using the Spring-Boot runnable jar. The tests are then executed from 'outside'
and call the REST services exposed by the Docker container.

To run the test start:
```
mvn test -P test
```
### C-O-M tests
C-O-M tests build a Docker image using the Spring-Boot runnable jar. The tests are then executed from 'outside'
and call the REST services exposed by the Docker container. The tests are implemented using the Component Object Model of Arquillian

To run the test start:
```
mvn test -P com-test
```
