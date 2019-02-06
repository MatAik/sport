package com.sport.controller;

import com.sport.data.OptimalRouteJson;
import com.sport.service.CalculatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator")
@Api(value = "calculator", description = "Operations for gathering itinerary related data")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @ApiOperation(value = "Retrieve the optimal routes by fastest time and fewest stops", response = OptimalRouteJson.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Call succeeded, returning optimal routes or empty list"),
            @ApiResponse(code = 503, message = "Some backend service is not running or is returning exception")})
    @GetMapping(value = "/optimalroutes", produces = "application/json")
    public @ResponseBody
    OptimalRouteJson getRoutes(@RequestParam(value = "startCity") String startCity,
                               @RequestParam(value = "destinationCity") String destinationCity) {
        return calculatorService.getOptimalRoutes(startCity, destinationCity);
    }

}
