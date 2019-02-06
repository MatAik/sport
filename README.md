# Itinerary service

The project here is an itinerary service for the purpose of providing the best possible routes from origin city to a destination. Both the travel time and the number stops are considered and the best options for either criteria are returned

The project consists of two microservices, one taking care of any access to database (called cityservice) and one that has the job of calculating the optimal routes based on the data from cityservice and possibly other services in the future.

There is also a registry service for handling the communication between services and option for load balancing.
