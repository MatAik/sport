package com.sport.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel(description = "Direct route between two cities")
public class RouteJson {

    public RouteJson(CityJson originCityJson, CityJson destinyCityJson, Date departureTime, Date arrivalTime) {
        this.originCityJson = originCityJson;
        this.destinyCityJson = destinyCityJson;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    @ApiModelProperty(notes = "City where the route begins")
    @JsonProperty("originCity")
    private CityJson originCityJson;

    @ApiModelProperty(notes = "City where the route ends")
    @JsonProperty("destinyCity")
    private CityJson destinyCityJson;

    @ApiModelProperty(notes = "Time of departure from origin city")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date departureTime;

    @ApiModelProperty(notes = "Time of arrival at destiny city")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date arrivalTime;

    @Override
    public String toString() {
        return "From: (" + originCityJson.toString() + ")" +
                " To: (" + destinyCityJson.toString() + ")" +
                " Departure: " + departureTime +
                " Arrival: " + arrivalTime;
    }
}
