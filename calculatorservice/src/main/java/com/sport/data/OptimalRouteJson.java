package com.sport.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel(description = "All optimal routes and time of calculation")
public class OptimalRouteJson {

    public OptimalRouteJson(Date timeOfCalculation, List<RouteJson> routes) {
        this.timeOfCalculation = timeOfCalculation;
        this.routes = routes;
    }

    @ApiModelProperty(notes = "Time when the route was calculated")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date timeOfCalculation;

    @ApiModelProperty(notes = "All routes returned, contains all fastest and easiest routes")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private List<RouteJson> routes;
}
