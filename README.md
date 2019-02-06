# Itinerary service

The project here is an itinerary service for the purpose of providing the best possible routes from origin city to a destination. Both the travel time and the number stops are considered and the best options for either criteria are returned

The project consists of two microservices, one taking care of any access to database (called cityservice) and one that has the job of calculating the optimal routes based on the data from cityservice and possibly other services in the future.

There is also a registry service for handling the communication between services and option for load balancing.

# Used frameworks and libraries

- Java 8

The project is build on Java 8

- Spring Boot

Spring Boot has been used for creating the microservices. Spring boot provides a fast framework for building single services with straight forward configuration possibilities

- Spring Cloud Netflix

Spring Cloud Netflix Eureka has been used to provide the application registry where the microservices sign in and where the microservices are able to find the locations of other services

- Docker

A Docker setup is provided for running the images of the microservices in containers and for easy installation to different servers and environments

- Springfox Swagger

A Swagger documendation has been included to provide understanding of the services and endpoints available

- H2 DB

The database service (cityservice) runs on in-memory H2 Database. This approach has been taken since there was a requirement to be able to run the application with Java and Maven only. There was a plan to extend this to PostgresSQL, but there was no time in the end.

- FasterXML / Jackson

Jackson libraries are used for JSON to Object mapping

