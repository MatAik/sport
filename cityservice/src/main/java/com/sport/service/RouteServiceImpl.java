package com.sport.service;

import com.sport.data.CityJson;
import com.sport.data.RouteJson;
import com.sport.entity.Route;
import com.sport.repository.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RouteServiceImpl implements RouteService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private RouteRepository routeRepository;

    @Autowired
    public void setRouteRepository(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }

    /**
     * Finds all routes and builds Json objects to be returned
     * @return
     */
    @Override
    public List<RouteJson> getRoutes() {

        log.info("Fetching all routes");

        ArrayList<RouteJson> routeJsons = new ArrayList<>();

        List<Route> routes = routeRepository.findAll();

        for (Route route : routes) {
            final RouteJson routeJson = new RouteJson(new CityJson(route.getStartCity().getName()),
                    new CityJson(route.getDestinyCity().getName()),
                    route.getDepartureTime(),
                    route.getArrivalTime());

            routeJsons.add(routeJson);
        }

        log.info("Found " + routeJsons.size() + " routes");

        return routeJsons;

    }
}
