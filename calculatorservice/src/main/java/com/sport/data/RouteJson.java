package com.sport.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sport.enums.RouteType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalTime;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel(description = "Complete route from destination A to B")
public class RouteJson {

    public RouteJson(RouteType routeType, long totalDays, LocalTime totalTime, List<SubRouteJson> routes) {
        this.routeType = routeType;
        this.totalDays = totalDays;
        this.totalTime = totalTime;
        this.routes = routes;
    }

    @ApiModelProperty(notes = "Route type, indicating if it's easy or fast")
    @JsonProperty("type")
    private RouteType routeType;

    @ApiModelProperty(notes = "Days spent travelling (waiting time not counted)")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long totalDays;

    @ApiModelProperty(notes = "Time spent travelling (waiting time not counted) in addition to days")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalTime totalTime;

    @ApiModelProperty(notes = "All subroutes taken")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private List<SubRouteJson> routes;
}
