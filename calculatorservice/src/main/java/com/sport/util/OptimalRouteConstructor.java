package com.sport.util;

import com.sport.data.OptimalRouteJson;
import com.sport.data.RouteJson;
import com.sport.data.SubRouteJson;
import com.sport.enums.RouteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OptimalRouteConstructor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Calculator method to process all given routes and return the best ones
     * @param subRoutes all subRoutes that can be used
     * @param startCity city of origin
     * @param destinationCity city to get to
     * @return the best routes possible, multiple easiest or fastest routes can be returned if they are equal
     */
    public OptimalRouteJson calculateOptimalRoutes(List<SubRouteJson> subRoutes, String startCity, String destinationCity) {

        log.info("Calculating optimal routes from " + startCity + " to " + destinationCity);

        final List<CalculatedSubRoutes> allPossibleRoutes = findPossibleRoutes(subRoutes, startCity, destinationCity);

        log.info("Found " + allPossibleRoutes.size() + " possible routes");

        final List<CalculatedSubRoutes> fastestRoutes = findFastestRoutes(allPossibleRoutes);
        final List<CalculatedSubRoutes> easiestRoutes = findEasiestRoutes(allPossibleRoutes);

        final List<RouteJson> optimalRoutes = new ArrayList<>();

        for (CalculatedSubRoutes fastestRoute : fastestRoutes) {
            final RouteJson fastestRouteJson = new RouteJson(RouteType.FASTEST, fastestRoute.getDays(), fastestRoute.getTime(), fastestRoute.getSubroutes());
            optimalRoutes.add(fastestRouteJson);
        }

        for (CalculatedSubRoutes easiestRoute : easiestRoutes) {
            final RouteJson easiestRouteJson = new RouteJson(RouteType.EASIEST, easiestRoute.getDays(), easiestRoute.getTime(), easiestRoute.getSubroutes());
            optimalRoutes.add(easiestRouteJson);
        }

        OptimalRouteJson optimalRoute = new OptimalRouteJson(new Date(System.currentTimeMillis()), optimalRoutes);

        log.info("Returning " + optimalRoutes.size() + " optimal routes");

        return optimalRoute;
    }

    /**
     * Figure out the routes with less stops
     * @param allPossibleRoutes every route possible
     * @return all routes with less stops (can be many if equal in number of stops)
     */
    public List<CalculatedSubRoutes> findEasiestRoutes(List<CalculatedSubRoutes> allPossibleRoutes) {
        List<CalculatedSubRoutes> easiestSubRoutes = new ArrayList<>();

        int easiestStops = Integer.MAX_VALUE;

        for (CalculatedSubRoutes possibleRoute : allPossibleRoutes) {

            // Adding to list if easier or as easy as easiest route so far
            if (easiestStops > possibleRoute.getSubroutes().size()) {
                easiestSubRoutes = new ArrayList<>();
                easiestSubRoutes.add(possibleRoute);
                easiestStops = possibleRoute.getSubroutes().size();
            } else if (easiestStops == possibleRoute.getSubroutes().size()) {
                easiestSubRoutes.add(possibleRoute);
            }
        }
        return easiestSubRoutes;
    }

    /**
     * Figure out the routes with less travel time
     * @param allPossibleRoutes every route possible
     * @return all routes with less travel time (can be many if equal in time)
     */
    public List<CalculatedSubRoutes> findFastestRoutes(List<CalculatedSubRoutes> allPossibleRoutes) {
        List<CalculatedSubRoutes> fastestSubRoutes = new ArrayList<>();
        CalculatedSubRoutes fastestRoute = null;

        for (CalculatedSubRoutes possibleRoute : allPossibleRoutes) {

            if (fastestRoute == null || fastestRoute.compareTo(possibleRoute) > 0) {
                // reset the list if this is faster than any so far
                fastestSubRoutes = new ArrayList<>();
                fastestSubRoutes.add(possibleRoute);
                fastestRoute = possibleRoute;
            } else if (fastestRoute.compareTo(possibleRoute) == 0) {
                // if equal to fastest so far, keep all in list
                fastestSubRoutes.add(possibleRoute);
            }
        }
        return fastestSubRoutes;
    }

    /**
     * Find all possible routes from start city to destination city
     * One city can only be visited once in every route
     *
     * @param subRoutes All subroutes available
     * @param startCity City of origin
     * @param destinationCity City to get to
     * @return All unique routes from startCity to destinationCity
     */
    public List<CalculatedSubRoutes> findPossibleRoutes(List<SubRouteJson> subRoutes, String startCity, String destinationCity) {

        List<CalculatedSubRoutes> foundRoutes = new ArrayList<>();

        List<CalculatedSubRoutes> possibleRoutes = new ArrayList<>();

        List<SubRouteJson> routesNotUsed = new ArrayList<>();

        for (SubRouteJson subRoute : subRoutes) {
            if (subRoute.getStartCity().equals(startCity)) {
                if (subRoute.getDestinationCity().equals(destinationCity)) {
                    addRoute(foundRoutes, subRoute);
                } else {
                    addRoute(possibleRoutes, subRoute);
                }
            } else {
                routesNotUsed.add(subRoute);
            }
        }

        if (!possibleRoutes.isEmpty()) {
            // if there are still routes without dead ends, let's continue
            return findPossibleRoutes(foundRoutes, possibleRoutes, routesNotUsed, destinationCity);
        } else {
            return foundRoutes;
        }
    }

    /**
     * Recurring method to iterate through possible routes not reaching a dead end until no open ends found
     *
     * @param foundRoutes already verified routes
     * @param subRoutesFromCity subroutes that can still lead to destination
     * @param routesNotUsed all routes except those starting from origin
     * @param destinationCity city where we want to go to
     * @return All unique routes from startCity to destinationCity
     */
    private List<CalculatedSubRoutes> findPossibleRoutes(List<CalculatedSubRoutes> foundRoutes, final List<CalculatedSubRoutes> subRoutesFromCity, List<SubRouteJson> routesNotUsed, final String destinationCity) {

        List<CalculatedSubRoutes> newPossibleRoutesCity = new ArrayList<>();

        for (CalculatedSubRoutes routes : subRoutesFromCity) {

            for (SubRouteJson subRoute : routesNotUsed) {
                if (!routes.getCities().contains(subRoute.getDestinationCity())) {
                    if (routes.getCurrentCity().equals(subRoute.getStartCity())) {
                        CalculatedSubRoutes newSubRoute = new CalculatedSubRoutes();
                        newSubRoute.copySubRoute(routes);

                        // Update total time so far
                        newSubRoute.addTime(subRoute.getTime());

                        newSubRoute.getSubroutes().add(subRoute);

                        // Let's add the city to the list of cities visited so we don't visit it again
                        newSubRoute.getCities().add(subRoute.getDestinationCity());

                        if (destinationCity.equals(subRoute.getDestinationCity())) {
                            foundRoutes.add(newSubRoute);
                        } else {
                            // Not at destination yet, but still possible to get there
                            newSubRoute.setCurrentCity(subRoute.getDestinationCity());
                            newPossibleRoutesCity.add(newSubRoute);
                        }
                    }
                }
            }
        }

        if (!newPossibleRoutesCity.isEmpty()) {
            // if there are still routes without dead ends, let's continue
            return findPossibleRoutes(foundRoutes, newPossibleRoutesCity, routesNotUsed, destinationCity);
        } else {
            return foundRoutes;
        }
    }

    /**
     * Create a new route from a single subroute and add it to list of complete routes (can be verified or potential)
     *
     * @param routes list of routes
     * @param subRoute a subroute constructed into a calculated route and to be added to routes
     */
    private void addRoute(List<CalculatedSubRoutes> routes, SubRouteJson subRoute) {
        final List<SubRouteJson> subRouteJsons = new ArrayList<>();
        subRouteJsons.add(subRoute);
        final List<String> cities = new ArrayList<>();
        cities.add(subRoute.getStartCity());

        CalculatedSubRoutes route = new CalculatedSubRoutes(subRouteJsons, cities, subRoute.getDestinationCity());
        route.addTime(subRoute.getTime());

        routes.add(route);
    }


    /**
     * A util class for handling and comparing the routes
     */
    static class CalculatedSubRoutes implements Comparable<CalculatedSubRoutes> {

        CalculatedSubRoutes() {
        }

        CalculatedSubRoutes(List<SubRouteJson> subroutes, List<String> cities, String currentCity) {
            this.subroutes = subroutes;
            this.cities = cities;
            this.currentCity = currentCity;
            days = 0l;
            time = LocalTime.of(0, 0);
            totalTime = LocalDateTime.of(date, hours);
        }

        public CalculatedSubRoutes(List<SubRouteJson> subroutes, List<String> cities, String currentCity, long days, LocalTime time, LocalDateTime totalTime) {
            this.subroutes = subroutes;
            this.cities = cities;
            this.currentCity = currentCity;
            this.days = days;
            this.time = time;
            this.totalTime = totalTime;
        }

        public void copySubRoute(CalculatedSubRoutes toBeCopied) {
            this.subroutes = new ArrayList<>(toBeCopied.getSubroutes());
            this.cities = new ArrayList<>(toBeCopied.getCities());
            this.currentCity = toBeCopied.currentCity;
            this.days = toBeCopied.days;
            this.time = toBeCopied.time;
            this.totalTime = toBeCopied.totalTime;
        }

        private List<SubRouteJson> subroutes;

        private List<String> cities;

        private String currentCity;

        private final static LocalDate date = LocalDate.of(0, 1, 1);
        private final static LocalTime hours = LocalTime.of(0, 0);
        private final static LocalDateTime datetime = LocalDateTime.of(date, hours);

        private LocalDateTime totalTime;

        private long days;

        private LocalTime time;

        public List<SubRouteJson> getSubroutes() {
            return subroutes;
        }

        public List<String> getCities() {
            return cities;
        }

        public String getCurrentCity() {
            return currentCity;
        }

        public void setCurrentCity(String currentCity) {
            this.currentCity = currentCity;
        }

        public long getDays() {
            return days;
        }

        public LocalTime getTime() {
            return time;
        }

        public void addTime(LocalTime timeToBeAdded) {
            totalTime = totalTime.plus(Duration.ofNanos(timeToBeAdded.toNanoOfDay()));
            time = time.plus(Duration.ofNanos(timeToBeAdded.toNanoOfDay()));

            days = Duration.between(datetime, totalTime).toDays();
        }

        @Override
        public int compareTo(CalculatedSubRoutes o) {
            if (this.days > o.days) {
                return 1;
            } else if (this.days < o.days) {
                return -1;
            } else {
                if (this.time.isAfter(o.time)) {
                    return 1;
                } else if (this.time.isBefore(o.time)) {
                    return -1;
                }
            }
            return 0;
        }
    }
}
