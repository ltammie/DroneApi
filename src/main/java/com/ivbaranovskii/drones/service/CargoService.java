package com.ivbaranovskii.drones.service;

import com.ivbaranovskii.drones.entity.Cargo;
import com.ivbaranovskii.drones.entity.Drone;
import com.ivbaranovskii.drones.exception.EntityNotFoundException;
import com.ivbaranovskii.drones.repository.CargoRepository;
import com.ivbaranovskii.drones.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final DroneRepository droneRepository;

    @Transactional
    public List<Cargo> getCargoByDroneId(Long id) {
        Drone drone = droneRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(String.format("Drone with id: %d not found", id)));
        return cargoRepository.findByDroneId(drone.getId());
    }
}
