package com.ivbaranovskii.drones.enums;

import java.util.List;

public enum DroneState {
    IDLE,
    LOADING,
    LOADED,
    DELIVERING,
    DELIVERED,
    RETURNING;

    public Boolean isReadyForLoadingState() {
        return this == IDLE || this == LOADED;
    }

    public Boolean isNotReadyForLoadingState() {
        return !isReadyForLoadingState();
    }


    public static List<DroneState> getReadyForLoadingStates() {
        return List.of(IDLE, LOADED);
    }
}
