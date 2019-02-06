package com.sport.service;

import com.sport.config.DataServiceProperties;
import com.sport.data.OptimalRouteJson;
import com.sport.data.SubRouteJson;
import com.sport.integration.CityServiceHandler;
import com.sport.util.OptimalRouteConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableConfigurationProperties(DataServiceProperties.class)
public class CalculatorServiceImpl implements CalculatorService {

    private CityServiceHandler cityServiceHandler;

    private OptimalRouteConstructor optimalRouteConstructor;

    @Autowired
    public void setCityServiceHandler(CityServiceHandler cityServiceHandler){
        this.cityServiceHandler = cityServiceHandler;
    }

    @Autowired
    public void setOptimalRouteConstructor(OptimalRouteConstructor optimalRouteConstructor){
        this.optimalRouteConstructor = optimalRouteConstructor;
    }

    /**
     * Collects all possible routes from other service(s) and returns optimal routes after calculation
     * @param startCity city of origin
     * @param destinationCity city to go to
     * @return Optimal routes from start city to destination city
     */
    @Override
    public OptimalRouteJson getOptimalRoutes(String startCity, String destinationCity) {

        final List<SubRouteJson> subRoutes = cityServiceHandler.fetchRoutes();

        return optimalRouteConstructor.calculateOptimalRoutes(subRoutes, startCity, destinationCity);

    }

}
