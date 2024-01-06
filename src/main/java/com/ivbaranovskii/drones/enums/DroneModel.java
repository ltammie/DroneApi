package com.ivbaranovskii.drones.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DroneModel {
    LIGHT_WEIGHT("Lightweight"),
    MIDDLE_WEIGHT("Middleweight"),
    CRUISER_WEIGHT("Cruiserweight"),
    HEAVY_WEIGHT("Heavyweight");

    private final String model;

    DroneModel(String model) {
        this.model = model;
    }

    @JsonValue
    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return model;
    }
}
