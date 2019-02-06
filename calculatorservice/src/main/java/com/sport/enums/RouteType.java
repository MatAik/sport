package com.sport.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel(description = "Type of route EASY/FAST")
public enum RouteType {
    FASTEST("FAST"),
    EASIEST("EASY");

    @ApiModelProperty(notes = "Type of route FAST/EASY")
    private String representation;

    RouteType(String representation) {
        this.representation = representation;
    }

    @JsonValue
    public String getRepresentation() {
        return representation;
    }
}
