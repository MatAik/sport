package com.sport.service;

import com.sport.data.RouteJson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteServiceImplTest {

    private RouteService routeService;

    @Autowired
    public void getRouteService(RouteService routeService){
        this.routeService = routeService;
    }

    @Test
    public void getRoutes() {
        final List<RouteJson> routes = routeService.getRoutes();

        Assert.assertEquals(24, routes.size());
        Assert.assertEquals("From: (Name: Barcelona) To: (Name: Zaragoza) Departure: 12:49:00 Arrival: 11:44:00", routes.get(0).toString());
    }
}