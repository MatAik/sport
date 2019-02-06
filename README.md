# Itinerary service

The project here is an itinerary service for the purpose of providing the best possible routes from origin city to a destination. Both the travel time and the number stops are considered and the best options for either criteria are returned.

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

# Setup Instructions

The project can be run locally following the instructions below:

- Clone or download and unzip the repository from GitHub

- in the itinerary (or which ever directory the project was unzipped to) directory run the clean install command, for example
  - C:\sport-master>mvn clean install
  
- in the above directory run the command for all three services
  - C:\sport-master>java -jar ./registry/target/registry.jar
  - C:\sport-master>java -jar ./cityservice/target/cityservice.jar
  - C:\sport-master>java -jar ./calculatorservice/target/calculatorservice.jar
  
That's it! Setup is done and the projects are running!

# Setup Instructions for docker

The project can also be run using Docker containers. In order to publish with docker, follow the below instructions:

- Clone or download as above

- in the itinerary (or which ever directory the project was unzipped to) directory run the clean install command, for example
  - C:\sport-master>mvn clean package
  
- run the below build commands
  - docker build -t sport/cityservice ./cityservice
  - docker build -t sport/calculatorservice ./calculatorservice
  - docker build -t sport/registry ./registry
  
- to check that everything is according to plan so far run the command 'docker image' and the data similar to below should be shown

```
REPOSITORY                        TAG                 IMAGE ID            CREATED             SIZE
sport/registry                    latest              7a6b9c7a3cae        27 seconds ago      147MB
sport/calculatorservice           latest              4c176184fb24        44 seconds ago      151MB
sport/cityservice                 latest              25b7e7241360        52 seconds ago      165MB
```

- run the below commands to start all the services
  - docker run --rm -P --name sport-service-registry -p 8888:8888 sport/registry
  - docker run --rm -P --name sport-cityservice --link sport-service-registry -p 8666:8666 sport/cityservice
  - docker run --rm -P --name sport-calculatorservice --link sport-service-registry -p 8777:8777 sport/calculatorservice
  
  - link command is used since there was no time yet to provide a proper docker-compose.yml which could be used all the way to production
  
- to see that the services are running, give a command 'docker ps' and something like this should be shown:

```
CONTAINER ID        IMAGE                     COMMAND                  CREATED             STATUS              PORTS                                             NAMES
c552dedda9bd        sport/calculatorservice   "java -Djava.securit…"   About an hour ago   Up About an hour    0.0.0.0:8777->8777/tcp, 0.0.0.0:32780->8080/tcp   sport-calculatorservice
b2472292b627        sport/cityservice         "java -Djava.securit…"   About an hour ago   Up About an hour    0.0.0.0:8666->8666/tcp, 0.0.0.0:32779->8080/tcp   sport-cityservice
5f5bbf16736a        sport/registry            "java -Djava.securit…"   About an hour ago   Up About an hour    0.0.0.0:8888->8888/tcp                            sport-service-registry

```

# Usage and details

- Calculator service

The calculator service has one GET endpoint. There are restrictions for the cityservice (and other future services with access to sensitive data). However, for the calculation service there are no security restrictions given as the service is intended to be available to anyone (user or service) who wants to use the service.

The end point address in local environment is for example http://localhost:8777/calculator/optimalroutes?startCity=Zaragoza&destinationCity=Barcelona giving in return the optimal routes available. When the service is running, detailed Api documentation can be found at http://localhost:8777/swagger-ui.html

The calculator service calls the registry service to retrieve the URL for cityservice. If the URL is not available or the registry is down, calculatorservice will try to call the service based on set up in dataservice.properties (this is to make for easier local testing if the registry is not available). This can also be observed in logging as below:

```
2019-02-06 18:08:15.542  INFO 38996 --- [nio-8777-exec-4] c.sport.integration.CityServiceHandler   : DataService address not found in registry. Configured direct address used
2019-02-06 18:08:15.542  INFO 38996 --- [nio-8777-exec-4] c.sport.integration.CityServiceHandler   : Calling url http://localhost:8666/routes


2019-02-06 18:08:18.625  INFO 38996 --- [nio-8777-exec-7] c.sport.integration.CityServiceHandler   : DataService address found in registry
2019-02-06 18:08:18.626  INFO 38996 --- [nio-8777-exec-7] c.sport.integration.CityServiceHandler   : Calling url http://192.168.99.1:8666/routes
```


With docker or when the registry is running locally the base address given from registry will be used to call the /routes end-point of cityservice.

Calculatorservice accesses the cityservice using basic authentication. If the username and password are set-up correctly (set by config by default) cityservice returns a list of all possible routes.

Calculator service uses the data given by cityservice to do calculation of the quickest and easiest routes available and returns them to the caller in json format. For the quickest route only travel time is considered and any waiting time is discarded.

- City service

The city service provides one GET endpoint for any identified users the fetch (and in the future possibly update) data from its database. The local address of the endpoint is http://localhost:8666/routes. Detailed info can be found at http://localhost:8666/swagger-ui.html.

Access to cityservice is restricted since the service provides access to vital data inside the system. Used authentication method is basic authentication. When viewed in browser it will ask for user credentials which are by default **sport** (username) and **city** (password). The future plan is to extend this to use tokens and to use different roles for different operations.

There is a H2 Database used by the service. The database consists of two tables, one which contains the city information and the other the routes between any registered cities. All routes have a starting city and a destination city (not two-way routes). For every route there is a departure time and arrival time. There is only one route between two cities, since the travel method is always the same. The default database comes with 24 routes.

The cityservice routes-endpoint is called without any parameters.

Cityservice accesses a database, does conversion to Json objects and returns all available routes
  
- Registry

The registry is a Eureka based service that is used by any microservices in the itinerary system to to register and gain information about other services. For cityservice and calculatorservice, the registry works both locally and in docker since both of those microservices have been assigned two default hostnames.
