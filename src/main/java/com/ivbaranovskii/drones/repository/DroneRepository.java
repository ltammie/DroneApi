package com.ivbaranovskii.drones.repository;

import com.ivbaranovskii.drones.entity.Drone;
import com.ivbaranovskii.drones.enums.DroneState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Page<Drone> findByStateInAndBatteryGreaterThanEqual(Pageable pageable, List<DroneState> state, float battery);
}
