package com.sport.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.sport.data.SubRouteJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A util class for mapping city connections to subroutes that can be handled by calculator
 */
public class SubRouteJsonMapper {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public List<SubRouteJson> convertToSubRoutes(InputStream content) throws IOException {

        final BufferedReader in = new BufferedReader(new InputStreamReader(content));

        final ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        final List<TemporaryRoute> temporaryRoutes = mapper.readValue(in, new TypeReference<List<TemporaryRoute>>() {});

        log.info("Found " + temporaryRoutes.size() + " routes from called database service");
        List<SubRouteJson> subRoutes = new ArrayList<>();

        for (TemporaryRoute temporaryRoute : temporaryRoutes) {

            LocalTime duration;

            // If arrival time is before departure time, the arrival is on the next day
            // Assumption is that no direct journey between two cities takes 1 day or more
            if (temporaryRoute.getDepartureTime().isBefore(temporaryRoute.getArrivalTime())) {
                duration = LocalTime.ofNanoOfDay(Duration.between(temporaryRoute.getDepartureTime(), temporaryRoute.getArrivalTime()).toNanos());
            } else {
                duration = LocalTime.ofNanoOfDay(Duration.between(temporaryRoute.getDepartureTime().minusNanos(1), LocalTime.MAX).toNanos());
                duration = duration.plusNanos(temporaryRoute.getArrivalTime().toNanoOfDay());
            }

            SubRouteJson subRoute = new SubRouteJson(temporaryRoute.getStartCity(), temporaryRoute.getDestinationCity(), duration);
            subRoutes.add(subRoute);
        }

        log.info("Returning " + subRoutes.size() + " subroutes");

        return subRoutes;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private static class TemporaryRoute {

        String startCity;

        @JsonProperty("originCity")
        private void unpackNameFromOriginCity(Map<String, String> city) {
            startCity = city.get("name");
        }

        String destinationCity;

        @JsonProperty("destinyCity")
        private void unpackNameFromDestinyCity(Map<String, String> city) {
            destinationCity = city.get("name");
        }

        @JsonProperty("departureTime")
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = LocalTimeDeserializer.class)
        private LocalTime departureTime;

        @JsonProperty("arrivalTime")
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = LocalTimeDeserializer.class)
        private LocalTime arrivalTime;

        String getStartCity() {
            return startCity;
        }

        String getDestinationCity() {
            return destinationCity;
        }

        LocalTime getDepartureTime() {
            return departureTime;
        }

        LocalTime getArrivalTime() {
            return arrivalTime;
        }
    }
}