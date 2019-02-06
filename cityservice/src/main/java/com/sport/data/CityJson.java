package com.sport.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel(description = "City")
public class CityJson {

    public CityJson(String name) {
        this.name = name;
    }

    @ApiModelProperty(notes = "Name of the city")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String name;

    @Override
    public String toString() {
        return "Name: " + name;
    }

}
