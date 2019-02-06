package com.sport.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ROUTE")
public class Route {

    @Id
    @ApiModelProperty(notes = "Generated DB id")
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ApiModelProperty(notes = "Start City id")
    @ManyToOne
    @JoinColumn(name = "START_CITY_ID", nullable = false)
    private City startCity;

    @ApiModelProperty(notes = "Destiny City id")
    @ManyToOne
    @JoinColumn(name = "DESTINY_CITY_ID", nullable = false)
    private City destinyCity;

    @ApiModelProperty(notes = "Departure time from start city")
    @Column(name = "DEPARTURE_TIME")
    @Temporal(TemporalType.TIME)
    private Date departureTime;

    @ApiModelProperty(notes = "Arrival time to destiny city")
    @Column(name = "ARRIVAL_TIME")
    @Temporal(TemporalType.TIME)
    private Date arrivalTime;

    public Long getId() {
        return id;
    }

    public City getStartCity() {
        return startCity;
    }

    public City getDestinyCity() {
        return destinyCity;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }
}
