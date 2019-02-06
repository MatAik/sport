package com.sport.controller;

import com.sport.data.RouteJson;
import com.sport.service.RouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@Api(value = "database", description = "Operations for fetching raw or combined data")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @ApiOperation(value = "Retrieve all the routes available")
    @GetMapping(value = "/routes", produces = "application/json")
    @PreAuthorize("hasAuthority('CALCULATOR_USER')")
    public @ResponseBody
    List<RouteJson> getRoutes() {
        return routeService.getRoutes();
    }

}
