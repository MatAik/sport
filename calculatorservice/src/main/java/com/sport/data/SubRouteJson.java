package com.sport.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalTime;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel(description = "Direct route between two cities")
public class SubRouteJson {

    public SubRouteJson(String startCity, String destinationCity, LocalTime time) {
        this.startCity = startCity;
        this.destinationCity = destinationCity;
        this.time = time;
    }

    @ApiModelProperty(notes = "Departure city")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String startCity;

    @ApiModelProperty(notes = "Arrival city")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String destinationCity;

    @ApiModelProperty(notes = "Time taken to travel between cities")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;

    public String getStartCity() {
        return startCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public LocalTime getTime() {
        return time;
    }
}
