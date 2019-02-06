package com.sport.service;

import com.sport.data.OptimalRouteJson;

public interface CalculatorService {

    OptimalRouteJson getOptimalRoutes(String startCity, String destinationCity);

}
