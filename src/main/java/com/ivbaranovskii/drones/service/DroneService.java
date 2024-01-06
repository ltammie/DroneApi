package com.ivbaranovskii.drones.service;

import com.ivbaranovskii.drones.dto.MedicineDto;
import com.ivbaranovskii.drones.entity.Cargo;
import com.ivbaranovskii.drones.entity.Drone;
import com.ivbaranovskii.drones.entity.Medicine;
import com.ivbaranovskii.drones.enums.DroneState;
import com.ivbaranovskii.drones.exception.BadRequestException;
import com.ivbaranovskii.drones.exception.EntityNotFoundException;
import com.ivbaranovskii.drones.mapper.DroneMapper;
import com.ivbaranovskii.drones.repository.CargoRepository;
import com.ivbaranovskii.drones.repository.DroneRepository;
import com.ivbaranovskii.drones.repository.MedicineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final MedicineRepository medicineRepository;
    private final CargoRepository cargoRepository;
    private final DroneMapper droneMapper;


    public DroneService(DroneRepository droneRepository, MedicineRepository medicineRepository, CargoRepository cargoRepository,
                        DroneMapper droneMapper) {
        this.droneRepository = droneRepository;
        this.medicineRepository = medicineRepository;
        this.cargoRepository = cargoRepository;
        this.droneMapper = droneMapper;
    }

    @Transactional
    public Drone getDroneById(Long id) {
        return droneRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(String.format("Drone with id: %d not found", id)));
    }

    @Transactional
    public Page<Drone> getAvailableDrones(Pageable pageable) {
        return droneRepository.findByStateInAndBatteryGreaterThanEqual(
                pageable,
                DroneState.getReadyForLoadingStates(),
                25
        );
    }

    @Transactional
    public Drone saveDrone(Drone droneToSave) {
        return droneRepository.save(droneToSave);
    }

    @Transactional
    public void loadDroneWithCargo(Long id, List<MedicineDto> medicines) {
        Drone drone = droneRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(String.format("Drone with id: %d not found", id)));

        if (drone.getBattery() < 25) {
            throw new BadRequestException(String.format("Drone with id: %d battery level < 25%%", id));
        }

        if (drone.getState().isNotReadyForLoadingState()) {
            throw new BadRequestException(String.format("Drone with id: %d is not available for loading", id));

        }

        List<Medicine> newMedicines = medicines.stream()
                .map(droneMapper::toEntity)
                .toList();
        Double weightToAdd = newMedicines.stream()
                .mapToDouble(Medicine::getWeight)
                .sum();
        Double weightSaved = cargoRepository.findSumOfMedicineWeightForDrone(id);

        if (weightToAdd + weightSaved > drone.getWeightLimit()) {
            throw new BadRequestException(String.format("Weight of added cargo exceeds weight limit of Drone with id: %d", id));

        }

        List<Medicine> savedMedicines = medicineRepository.saveAll(newMedicines);
        List<Cargo> savedCargos = savedMedicines.stream()
                .map(medicine -> new Cargo(drone, medicine))
                .collect(Collectors.toList());

        cargoRepository.saveAll(savedCargos);

        drone.setState(DroneState.LOADING);
    }
}
