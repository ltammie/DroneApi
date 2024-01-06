package com.ivbaranovskii.drones.controller;

import com.ivbaranovskii.drones.dto.DroneDto;
import com.ivbaranovskii.drones.dto.MedicineDto;
import com.ivbaranovskii.drones.entity.Cargo;
import com.ivbaranovskii.drones.entity.Drone;
import com.ivbaranovskii.drones.exception.UnprocessableEntityException;
import com.ivbaranovskii.drones.mapper.DroneMapper;
import com.ivbaranovskii.drones.service.CargoService;
import com.ivbaranovskii.drones.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
class DroneController {

    private final DroneService droneService;
    private final CargoService cargoService;
    private final DroneMapper droneMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get a drone by its id")
    private DroneDto getDroneById(@PathVariable @Parameter(name = "id", description = "id of drone to be searched") Long id) {
        return droneMapper.toDto(droneService.getDroneById(id));
    }

    @GetMapping("/available")
    @Operation(summary = "Get all drones available for loading cargo")
    private Page<DroneDto> getAvailableDrones(
            @Parameter(name = "pageable", description = "Pagination parameters for controlling the result set") Pageable pageable) {
        return droneService
                .getAvailableDrones(pageable)
                .map(droneMapper::toDto);
    }

    @PostMapping("")
    @Operation(summary = "Register drone")
    private ResponseEntity<Void> registerDrone(
            @RequestBody @Parameter(name = "newDrone", description = "drone to be saved") DroneDto newDrone,
            UriComponentsBuilder ucb) {
        Drone savedDrone = droneService.saveDrone(droneMapper.toEntity(newDrone));
        URI savedDroneLocation = ucb
                .path("/drones/{id}")
                .buildAndExpand(savedDrone.getId())
                .toUri();
        return ResponseEntity.created(savedDroneLocation).build();
    }

    @GetMapping("/{id}/battery")
    @Operation(summary = "Get value of drone battery level")
    private ResponseEntity<String> checkDroneBattery(@PathVariable @Parameter(name = "id", description = "id of drone to be searched") Long id) {
        Drone drone = droneService.getDroneById(id);
        return ResponseEntity.ok("{\"battery\":" + drone.getBattery() + "}");
    }

    @PostMapping("/{id}/medicine")
    @Operation(summary = "Load drone with medicine")
    private ResponseEntity<Void> loadDroneWithCargo(
            @PathVariable @Parameter(name = "id", description = "id of drone to be loaded") Long id,
            @RequestBody @Parameter(name = "medicineList", description = "list of medicine to be loaded") List<MedicineDto> medicineList,
            UriComponentsBuilder ucb
    ) {
        if (medicineList.isEmpty())
            throw new UnprocessableEntityException("Medicine list is empty");

        droneService.loadDroneWithCargo(id, medicineList);
        URI droneCargoLocation = ucb
                .path("/drones/{id}/medicine")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(droneCargoLocation).build();
    }

    @GetMapping("/{id}/medicine")
    private List<MedicineDto> getCargoByDroneId(
            @PathVariable @Parameter(name = "id", description = "id of drone to be searched for medicine") Long id
    ) {
        List<Cargo> cargos = cargoService.getCargoByDroneId(id);
        return cargos
                .stream()
                .map(Cargo::getMedicine)
                .map(droneMapper::toDto)
                .collect(Collectors.toList());
    }
}
